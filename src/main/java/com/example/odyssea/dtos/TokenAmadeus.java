package com.example.odyssea.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class TokenAmadeus {

    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("access_token")
    private String Token;
    @JsonProperty("expires_in")
    private String expiresIn; // Temps d'expiration en secondes
    private Instant expiryTime; // Date d'expiration


    public TokenAmadeus(String tokenType, String token, String expiresIn) {
        this.tokenType = tokenType;
        Token = token;
        this.expiresIn = expiresIn;
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

    public int getExpiresIn(String expiresIn) {
        return Integer.parseInt(expiresIn);
    }

    public String getExpiresInString(){
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Instant getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Instant expiryTime) {
        this.expiryTime = expiryTime;
    }
}
