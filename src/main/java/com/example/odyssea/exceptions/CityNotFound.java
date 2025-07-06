package com.example.odyssea.exceptions;

public class CityNotFound extends RuntimeException {
  public CityNotFound(String message) {
    super(message);
  }
}
