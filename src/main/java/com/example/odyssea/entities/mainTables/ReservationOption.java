package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.NotNull;

public class ReservationOption {

    @NotNull(message = "User ID is required")
    private int reservationId;
    @NotNull(message = "Option ID is required")
    private int optionId;

    public ReservationOption() {
    }


    public int getItineraryId() {
        return reservationId;
    }

    public void setItineraryId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
}
