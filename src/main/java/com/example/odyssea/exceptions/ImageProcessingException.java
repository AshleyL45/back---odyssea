package com.example.odyssea.exceptions;

public class ImageProcessingException extends RuntimeException {
    public ImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
    public ImageProcessingException(String message) {
        super(message);
    }
}
