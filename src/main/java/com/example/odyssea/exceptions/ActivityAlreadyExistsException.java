package com.example.odyssea.exceptions;

public class ActivityAlreadyExistsException extends RuntimeException {

    public ActivityAlreadyExistsException(String message) {
        super(message);
    }

    public ActivityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivityAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
