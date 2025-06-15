package com.example.odyssea.entities.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingDraft {
    private int draftId;
    private Integer userId;
    private Integer itineraryId;
    private LocalDate departureDate;
    private LocalDate returnDate;     // ← assurez-vous qu’elle existe comme champ
    private int numberOfAdults;
    private int numberOfKids;
    private LocalDateTime createdAt;
    private String type;

    public BookingDraft() {
    }

    public BookingDraft(int draftId, Integer userId, Integer itineraryId, LocalDate departureDate, int numberOfAdults, int numberOfKids, LocalDateTime createdAt, String type) {
        this.draftId = draftId;
        this.userId = userId;
        this.itineraryId = itineraryId;
        this.departureDate = departureDate;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
        this.createdAt = createdAt;
        this.type = type;
    }


    public int getDraftId() {
        return draftId;
    }

    public void setDraftId(int draftId) {
        this.draftId = draftId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(Integer itineraryId) {
        this.itineraryId = itineraryId;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfKids() {
        return numberOfKids;
    }

    public void setNumberOfKids(int numberOfKids) {
        this.numberOfKids = numberOfKids;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getReturnDate() {
        return this.returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}

