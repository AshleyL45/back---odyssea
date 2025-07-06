package com.example.odyssea.entities.userItinerary.drafts;

import java.time.LocalDate;

public class UserItineraryDraft {
    private Integer id;
    private Integer userId;
    private Integer duration;
    private LocalDate startDate;
    private String departureCity;
    private Integer hotelStanding;
    private Integer numberAdults;
    private  Integer numberKids;
    private LocalDate createdAt;

    public UserItineraryDraft() {
    }

    public UserItineraryDraft(Integer id, Integer userId, Integer duration, LocalDate startDate, String departureCity, Integer hotelStanding, Integer numberAdults, Integer numberKids, LocalDate createdAt) {
        this.id = id;
        this.userId = userId;
        this.duration = duration;
        this.startDate = startDate;
        this.departureCity = departureCity;
        this.hotelStanding = hotelStanding;
        this.numberAdults = numberAdults;
        this.numberKids = numberKids;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public Integer getHotelStanding() {
        return hotelStanding;
    }

    public void setHotelStanding(Integer hotelStanding) {
        this.hotelStanding = hotelStanding;
    }

    public Integer getNumberAdults() {
        return numberAdults;
    }

    public void setNumberAdults(Integer numberAdults) {
        this.numberAdults = numberAdults;
    }

    public Integer getNumberKids() {
        return numberKids;
    }

    public void setNumberKids(Integer numberKids) {
        this.numberKids = numberKids;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
