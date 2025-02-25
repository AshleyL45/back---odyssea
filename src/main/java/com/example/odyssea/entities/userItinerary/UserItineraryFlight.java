package com.example.odyssea.entities.userItinerary;

public class UserItineraryFlight {
    private int userItineraryId;
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
