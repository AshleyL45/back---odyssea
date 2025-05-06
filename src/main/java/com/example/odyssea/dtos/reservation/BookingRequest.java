package com.example.odyssea.dtos.reservation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public class BookingRequest {

    @NotNull(message = "Itinerary id cannot be null.")
    private int itineraryId;
    @NotNull(message = "Departure date cannot be null.")
    private String departureDate;
    @NotNull(message = "Return date cannot be null.")
    private String returnDate;
    @Positive(message = "Number of adults must be positive.")
    private int numberOfAdults;
    @PositiveOrZero(message = "Number of kids must be positive or equal to zero.")
    private int numberOfKids;
    private List<Integer> optionIds;

    public BookingRequest() {
    }

    public BookingRequest(int itineraryId, String departureDate, String returnDate, int numberOfAdults, int numberOfKids, List<Integer> optionIds) {
        this.itineraryId = itineraryId;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
        this.optionIds = optionIds;
    }


    public int getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(Integer numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfKids() {
        return numberOfKids;
    }

    public void setNumberOfKids(Integer numberOfKids) {
        this.numberOfKids = numberOfKids;
    }

    public List<Integer> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(List<Integer> optionIds) {
        this.optionIds = optionIds;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public void setNumberOfKids(int numberOfKids) {
        this.numberOfKids = numberOfKids;
    }

}

