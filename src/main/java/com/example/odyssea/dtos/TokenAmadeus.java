package com.example.odyssea.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class TokenAmadeus {

    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("access_token")
    private String Token;
    @JsonProperty("expires_in")
    private int expiresIn; // Temps d'expiration en secondes
    private Instant expiryTime; // Date d'expiration


    public TokenAmadeus(String tokenType, String token, int expiresIn) {
        this.tokenType = tokenType;
        Token = token;
        this.expiresIn = expiresIn;
    }

    public TokenAmadeus() {
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
        this.expiryTime = Instant.now().plusSeconds(expiresIn); // Calcule la date d'expiration
    }

    public Instant getExpiryTime() {
        return expiryTime;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiryTime); // Vérifie si le token est expiré
    }
}
