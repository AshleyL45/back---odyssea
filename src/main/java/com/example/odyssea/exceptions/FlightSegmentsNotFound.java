package com.example.odyssea.exceptions;

public class FlightSegmentsNotFound extends RuntimeException {
    public FlightSegmentsNotFound(String message) {
        super(message);
    }
}
