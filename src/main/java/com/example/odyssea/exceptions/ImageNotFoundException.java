package com.example.odyssea.exceptions;

public class ImageNotFoundException extends RuntimeException {
  public ImageNotFoundException(String message) {
    super(message);
  }
}
