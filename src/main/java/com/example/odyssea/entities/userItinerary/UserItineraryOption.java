package com.example.odyssea.entities.userItinerary;

import jakarta.validation.constraints.Min;

public class UserItineraryOption {

    @Min(value = 1, message = "User Itinerary ID must be greater than or equal to 1")
    private int userItineraryId;

    @Min(value = 1, message = "Option ID must be greater than or equal to 1")
    private int optionId;

    public UserItineraryOption(int userItineraryId, int optionId) {
        this.userItineraryId = userItineraryId;
        this.optionId = optionId;
    }

    public int getUserItineraryId() {
        return userItineraryId;
    }

    public void setUserItineraryId(int userItineraryId) {
        this.userItineraryId = userItineraryId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
}