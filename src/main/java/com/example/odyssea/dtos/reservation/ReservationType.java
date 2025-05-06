package com.example.odyssea.dtos.reservation;

import jakarta.validation.constraints.Pattern;

public class ReservationType {
    @Pattern(regexp = "Standard|Mystery", message = "Type must be either 'Standard' or 'Mystery'")
    private String type;

    public ReservationType() {}

    public ReservationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
