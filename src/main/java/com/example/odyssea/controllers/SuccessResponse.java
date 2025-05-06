package com.example.odyssea.controllers;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public class SuccessResponse<T> {

    @Schema(description = "HTTP Status of the response")
    private HttpStatus httpStatus;

    @Schema(description = "Message describing the result of the operation")
    private String message;

    @Schema(description = "Data payload of the response")
    private T data;

    public SuccessResponse() {
    }

    public SuccessResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public SuccessResponse(HttpStatus httpStatus, String message, T data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

