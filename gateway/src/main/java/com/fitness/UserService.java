package com.fitness;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import user.RegisterRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    private final WebClient webClient;

    public UserService(@Qualifier("userServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Validates if a user exists in the local database.
     * Returns true if exists, false if NOT_FOUND.
     * Propagates other errors (e.g. Connection refused).
     */
    public Mono<Boolean> validateUser(String userId) {
        return webClient.get()
                .uri("/user/" + userId + "/validate")
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.just(false);
                    }
                    log.error("HTTP error while validating user: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                    return Mono.error(e);
                })
                .doOnError(e -> log.error("Error connecting to User-Service for validation: {}", e.getMessage()));
    }

    /**
     * Registers a new user in the local database.
     */
    public Mono<Void> registerUser(RegisterRequest registerRequest) {
        return webClient.post()
                .uri("/user/register")
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.info("Successfully synced user {} to local database", registerRequest.getKeycloakId()))
                .doOnError(e -> log.error("Failed to sync user to local database: {}", e.getMessage()));
    }
}
