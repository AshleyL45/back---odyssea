package com.example.odyssea.entities.itinerary;

import jakarta.validation.constraints.Min;

public class ItineraryOption {

    @Min(value = 1, message = "Reservation ID must be greater than or equal to 1")
    private int idReservation;

    @Min(value = 1, message = "Option ID must be greater than or equal to 1")
    private int idOption;

    public ItineraryOption() {}

    public ItineraryOption(int idReservation, int idOption) {
        this.idReservation = idReservation;
        this.idOption = idOption;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdOption() {
        return idOption;
    }

    public void setIdOption(int idOption) {
        this.idOption = idOption;
    }
}
