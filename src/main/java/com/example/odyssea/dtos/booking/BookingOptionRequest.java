package com.example.odyssea.dtos.booking;

public class BookingOptionRequest {

    private int itineraryId;
    private int optionId;

    public BookingOptionRequest() {
    }

    public BookingOptionRequest(int itineraryId, int optionId) {
        this.itineraryId = itineraryId;
        this.optionId = optionId;
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
