package com.example.odyssea.dtos;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Standard API Response")
public record ApiResponse<T>(
        @Schema(description = "Request success flag") boolean success,
        @Schema(description = "Human readable message") String message,
        @Schema(description = "Payload data") T data,
        @Schema(description = "HTTP status code") int status,
        @Schema(description = "Response timestamp") Instant timestamp
) {
    public static <T> ApiResponse<T> success(String message, T data, HttpStatus status) {
        return new ApiResponse<>(true, message, data, status.value(), Instant.now());
    }

    public static ApiResponse<Void> success(String message, HttpStatus status) {
        return new ApiResponse<>(true, message, null, status.value(), Instant.now());
    }

    public static ApiResponse<Void> error(String message, HttpStatus status) {
        return new ApiResponse<>(false, message, null, status.value(), Instant.now());
    }
}


