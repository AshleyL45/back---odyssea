package com.example.odyssea.exceptions.JwtToken;

public class JwtTokenSignatureException extends RuntimeException {
    public JwtTokenSignatureException(String message) {
        super(message);
    }
}
