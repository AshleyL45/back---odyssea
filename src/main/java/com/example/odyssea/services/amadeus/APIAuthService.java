package com.example.odyssea.services.amadeus;

import com.example.odyssea.dtos.amadeus.TokenAmadeus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class APIAuthService {

    private final WebClient webClient;

    @Value("${amadeus.client.id}")
    private String clientId;

    @Value("${amadeus.client.secret}")
    private String clientSecret;

    public APIAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://test.api.amadeus.com/").build();
    }

    public Mono<TokenAmadeus> loginToAmadeus() {
        System.out.println("Demande d'un nouveau token à Amadeus...");
        return webClient.post()
                .uri("v1/security/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret)
                .retrieve()
                .bodyToMono(TokenAmadeus.class)
                .doOnError(error -> System.err.println("Erreur lors de la demande de token: " + error.getMessage()));
    }
}
