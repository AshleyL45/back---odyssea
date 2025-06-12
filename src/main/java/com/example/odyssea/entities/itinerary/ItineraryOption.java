package com.example.odyssea.entities.itinerary;

import jakarta.validation.constraints.Min;

public class ItineraryOption {

    @Min(value = 1, message = "Booking ID must be greater than or equal to 1")
    private int idBooking;

    @Min(value = 1, message = "Option ID must be greater than or equal to 1")
    private int idOption;

    public ItineraryOption() {}

    public ItineraryOption(int idBooking, int idOption) {
        this.idBooking = idBooking;
        this.idOption = idOption;
    }

    public int getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(int idBooking) {
        this.idBooking = idBooking;
    }

    public int getIdOption() {
        return idOption;
    }

    public void setIdOption(int idOption) {
        this.idOption = idOption;
    }
}
