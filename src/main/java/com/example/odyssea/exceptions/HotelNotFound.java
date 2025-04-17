package com.example.odyssea.exceptions;

public class HotelNotFound extends RuntimeException {
    public HotelNotFound(String message) {
        super(message);
    }
}
