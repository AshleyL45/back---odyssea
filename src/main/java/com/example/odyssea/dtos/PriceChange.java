package com.example.odyssea.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class PriceChange {
    @NotNull(message = "The new price cannot be null.")
    @Positive(message = "The new price must be positive.")
    private BigDecimal newPrice;

    public PriceChange() {
    }

    public PriceChange(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }
}
