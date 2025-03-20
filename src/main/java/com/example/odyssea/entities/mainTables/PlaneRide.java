package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PlaneRide {

    @Min(value = 1, message = "Plane Ride ID must be greater than or equal to 1")
    private int id;

    private boolean oneWay;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price must be greater than or equal to 0")
    private BigDecimal totalPrice;

    @NotBlank(message = "Currency is required")
    @Size(max = 10, message = "Currency must not exceed 10 characters")
    private String currency;

    @NotNull(message = "Creation date is required")
    private LocalDateTime createdAt;

    public PlaneRide() {}

    public PlaneRide(int id, boolean oneWay, BigDecimal totalPrice, String currency, LocalDateTime createdAt) {
        this.id = id;
        this.oneWay = oneWay;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public boolean isOneWay() {
        return oneWay;
    }
    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
