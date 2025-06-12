package com.example.odyssea.dtos.booking;

import jakarta.validation.constraints.Positive;

public class ItineraryChoice {
    @Positive(message = "Itinerary ID must be positive.")
    private Integer itineraryId;

    public ItineraryChoice() {}

    public ItineraryChoice(Integer itineraryId) {
        this.itineraryId = itineraryId;
    }

    public Integer getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(Integer itineraryId) {
        this.itineraryId = itineraryId;
    }
}
