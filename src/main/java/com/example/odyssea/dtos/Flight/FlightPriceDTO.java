package com.example.odyssea.dtos.Flight;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class FlightPriceDTO {
    @JsonProperty("grandTotal")
    private BigDecimal totalPrice;

    public FlightPriceDTO() {
    }

    public FlightPriceDTO(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
