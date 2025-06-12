package com.example.odyssea.dtos.booking;

import jakarta.validation.constraints.Pattern;

public class BookingType {
    @Pattern(regexp = "Standard|Mystery", message = "Type must be either 'Standard' or 'Mystery'")
    private String type;

    public BookingType() {}

    public BookingType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
