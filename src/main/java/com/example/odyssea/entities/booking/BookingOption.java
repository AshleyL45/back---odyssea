package com.example.odyssea.entities.booking;

import jakarta.validation.constraints.NotNull;

public class BookingOption {

    @NotNull(message = "User ID is required")
    private int booking;
    @NotNull(message = "Option ID is required")
    private int optionId;

    public BookingOption() {
    }


    public int getItineraryId() {
        return booking;
    }

    public void setItineraryId(int booking) {
        this.booking = booking;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
}
