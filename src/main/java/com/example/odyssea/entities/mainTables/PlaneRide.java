package com.example.odyssea.entities.mainTables;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PlaneRide {

    @Id
    private int id;
    private boolean oneWay;
    private BigDecimal totalPrice;
    private String currency;
    private LocalDateTime createdAt;

    public PlaneRide() {
    }

    public PlaneRide(boolean oneWay, BigDecimal totalPrice, String currency, LocalDateTime createdAt) {
        this.oneWay = oneWay;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
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
