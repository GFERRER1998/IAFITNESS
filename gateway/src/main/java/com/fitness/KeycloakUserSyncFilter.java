package com.fitness;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import java.text.ParseException;
import user.RegisterRequest;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter {
    
    private final UserService userService;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        return Mono.fromCallable(() -> getUserDetails(authHeader))
                .flatMap(registerRequest -> {
                    String userId = registerRequest.getKeycloakId();
                    String email = registerRequest.getEmail();
                    if (userId == null) {
                        return chain.filter(exchange);
                    }

                    log.info("[Sync] Processing token for email: {}, KeycloakID: {}", email, userId);

                    return userService.validateUser(userId)
                            .flatMap(exists -> {
                                if (!exists) {
                                    log.info("[Sync] User {} not found in local DB, triggering registration sync...", userId);
                                    return userService.registerUser(registerRequest);
                                }
                                log.info("[Sync] User {} already exists in local DB, skipping sync.", userId);
                                return Mono.empty();
                            })
                            .then(Mono.defer(() -> {
                                log.info("[Sync] Injecting X-User-ID: {} for downstream services", userId);
                                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                        .header("X-User-ID", userId)
                                        .build();
                                return chain.filter(exchange.mutate().request(mutatedRequest).build());
                            }));
                })
                .onErrorResume(e -> {
                    log.error("[Sync] Error during user synchronization: {}", e.getMessage());
                    return chain.filter(exchange);
                });
    }

    private RegisterRequest getUserDetails(String token) throws ParseException {
        // Safe removal of Bearer prefix using regex to avoid breaking the token
        String tokenWithoutBearer = token.replaceFirst("(?i)^Bearer\\s+", "");
        SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        RegisterRequest request = new RegisterRequest();
        request.setEmail(claims.getStringClaim("email"));
        request.setKeycloakId(claims.getStringClaim("sub"));
        request.setFirstName(claims.getStringClaim("given_name"));
        request.setLastName(claims.getStringClaim("family_name"));
        request.setPassword("dummy@123123");

        log.debug("[Sync] Extracted from JWT: email={}, sub={}, firstName={}, lastName={}", 
                  request.getEmail(), request.getKeycloakId(), request.getFirstName(), request.getLastName());

        return request;
    }
}
