package com.fitness.aiservices.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class GeminiService {
    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getRecommendations(String details) {
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                                Map.of("text", details)
                        })
                }
        );

        try {
            return webClient.post()
                    .uri(geminiApiUrl)
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", geminiApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(status -> status.isError(), clientResponse -> 
                        clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                System.err.println("Gemini API Error: " + clientResponse.statusCode() + " - " + errorBody);
                                return Mono.<Throwable>error(new RuntimeException("Gemini API Error: " + errorBody));
                            })
                    )
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            return "Error generating recommendation: " + e.getMessage();
        }
    }
}