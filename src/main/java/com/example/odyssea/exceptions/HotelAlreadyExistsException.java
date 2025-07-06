package com.example.odyssea.exceptions;

public class HotelAlreadyExistsException extends RuntimeException {

    public HotelAlreadyExistsException(String message) {
        super(message);
    }

    public HotelAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public HotelAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
