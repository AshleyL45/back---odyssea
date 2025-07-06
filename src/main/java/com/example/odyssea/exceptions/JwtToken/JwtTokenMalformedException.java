package com.example.odyssea.exceptions.JwtToken;

public class JwtTokenMalformedException extends RuntimeException {
    public JwtTokenMalformedException(String message) {
        super(message);
    }
}