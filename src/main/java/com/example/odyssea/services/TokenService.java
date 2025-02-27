package com.example.odyssea.services;

import com.example.odyssea.dtos.TokenAmadeus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TokenService {
    private final APIAuthService apiAuthService;
    private final AtomicReference<TokenAmadeus> cachedToken = new AtomicReference<>();


    public TokenService(APIAuthService apiAuthService) {
        this.apiAuthService = apiAuthService;
    }

    public Mono<String> getValidToken(){
        TokenAmadeus token = cachedToken.get();

        if(token != null && Instant.now().isBefore(token.getExpiryTime())){
            return Mono.just(token.getToken());
        }

        return apiAuthService.loginToAmadeus()
                .doOnNext(
                        (newToken -> {
                            newToken.setExpiryTime(Instant.now().plusSeconds(newToken.getExpiresIn(newToken.getExpiresInString()))); // Stocke le temps d'expiration
                            cachedToken.set(newToken);
                        })
                )
                .map(TokenAmadeus::getToken);
    }
}
