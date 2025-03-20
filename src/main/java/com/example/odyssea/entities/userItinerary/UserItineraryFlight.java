package com.example.odyssea.entities.userItinerary;

import jakarta.validation.constraints.Min;

public class UserItineraryFlight {

    @Min(value = 1, message = "User Itinerary ID must be greater than or equal to 1")
    private int userItineraryId;

    @Min(value = 1, message = "Flight ID must be greater than or equal to 1")
    private int flightId;

    public UserItineraryFlight(int userItineraryId, int flightId) {
        this.userItineraryId = userItineraryId;
        this.flightId = flightId;
    }

    public int getUserItineraryId() {
        return userItineraryId;
    }

    public void setUserItineraryId(int userItineraryId) {
        this.userItineraryId = userItineraryId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }
}