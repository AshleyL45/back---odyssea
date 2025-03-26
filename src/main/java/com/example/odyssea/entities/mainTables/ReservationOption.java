package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.NotNull;

public class ReservationOption {

    @NotNull(message = "User ID is required")
    private int userId;

    @NotNull(message = "Itinerary ID is required")
    private int itineraryId;

    @NotNull(message = "Option ID is required")
    private int optionId;

    public ReservationOption() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
}
