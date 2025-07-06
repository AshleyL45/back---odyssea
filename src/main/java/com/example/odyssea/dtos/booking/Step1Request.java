package com.example.odyssea.dtos.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class Step1Request {

    @Positive(message = "Itinerary ID must be positive.")
    private Integer itineraryId;

    @Pattern(regexp = "Standard|Mystery", message = "Type must be either 'Standard' or 'Mystery'")
    private String type;

    @NotNull(message = "Date cannot be null.")
    // format attendu : dd/MM/yyyy
    private String date;

    public Step1Request() {}

    public Step1Request(Integer itineraryId, String type, String date) {
        this.itineraryId = itineraryId;
        this.type = type;
        this.date = date;
    }

    public Integer getItineraryId() {
        return itineraryId;
    }
    public void setItineraryId(Integer itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
