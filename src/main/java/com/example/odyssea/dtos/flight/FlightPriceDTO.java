package com.example.odyssea.dtos.flight;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class FlightPriceDTO {
    @JsonProperty("grandTotal")
    private BigDecimal totalPrice;

    @JsonProperty("currency")
    private String currency;

    public FlightPriceDTO() {
    }

    public FlightPriceDTO(BigDecimal totalPrice, String currency) {
        this.totalPrice = totalPrice;
        this.currency = currency;
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
}
