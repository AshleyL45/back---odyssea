package com.example.odyssea.exceptions.JwtToken;

public class JwtTokenUnsupportedException extends RuntimeException {
    public JwtTokenUnsupportedException(String message) {
        super(message);
    }
}
