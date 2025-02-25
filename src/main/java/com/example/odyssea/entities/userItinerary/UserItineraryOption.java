package com.example.odyssea.entities.userItinerary;

public class UserItineraryOption {
    private int userItineraryId;
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
