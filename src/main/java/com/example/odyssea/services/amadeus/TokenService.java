package com.example.odyssea.services.amadeus;

import com.example.odyssea.dtos.amadeus.TokenAmadeus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class TokenService {
    private final APIAuthService apiAuthService;
    private final AtomicReference<TokenAmadeus> cachedToken = new AtomicReference<>();

    public TokenService(APIAuthService apiAuthService) {
        this.apiAuthService = apiAuthService;
    }

    public Mono<String> getValidToken() {
        TokenAmadeus token = cachedToken.get();

        // Vérifie si le token en cache est valide
        if (token != null && !token.isExpired()) {
            System.out.println("Utilisation du token en cache: " + token.getToken());
            return Mono.just(token.getToken());
        }

        // Si le token est expiré ou absent, en demande un nouveau
        return apiAuthService.loginToAmadeus()
                .doOnNext(newToken -> {
                    cachedToken.set(newToken); // Met à jour le cache
                })
                .map(TokenAmadeus::getToken);
    }
}