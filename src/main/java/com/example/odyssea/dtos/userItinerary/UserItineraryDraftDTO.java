package com.example.odyssea.dtos.userItinerary;

import java.time.LocalDate;
import java.util.List;

public class UserItineraryDraftDTO {
    private Integer id;
    private Integer userId;
    private Integer duration;
    private LocalDate startDate;
    private String departureCity;
    private List<Integer> countriesIds;
    private List<Integer> citiesIds;
    private List<Integer> activitiesIds;
    private Integer hotelStanding;
    private Integer numberAdults;
    private  Integer numberKids;
    private List<Integer> optionsIds;

    public UserItineraryDraftDTO() {
    }

    public UserItineraryDraftDTO(Integer id, Integer userId, Integer duration, LocalDate startDate, String departureCity, List<Integer> countriesIds, List<Integer> citiesIds, List<Integer> activitiesIds, Integer hotelStanding, Integer numberAdults, Integer numberKids, List<Integer> optionsIds) {
        this.id = id;
        this.userId = userId;
        this.duration = duration;
        this.startDate = startDate;
        this.departureCity = departureCity;
        this.countriesIds = countriesIds;
        this.citiesIds = citiesIds;
        this.activitiesIds = activitiesIds;
        this.hotelStanding = hotelStanding;
        this.numberAdults = numberAdults;
        this.numberKids = numberKids;
        this.optionsIds = optionsIds;
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

    public List<Integer> getCountriesIds() {
        return countriesIds;
    }

    public void setCountriesIds(List<Integer> countriesIds) {
        this.countriesIds = countriesIds;
    }

    public List<Integer> getCitiesIds() {
        return citiesIds;
    }

    public void setCitiesIds(List<Integer> citiesIds) {
        this.citiesIds = citiesIds;
    }

    public List<Integer> getActivitiesIds() {
        return activitiesIds;
    }

    public void setActivitiesIds(List<Integer> activitiesIds) {
        this.activitiesIds = activitiesIds;
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

    public List<Integer> getOptionsIds() {
        return optionsIds;
    }

    public void setOptionsIds(List<Integer> optionsIds) {
        this.optionsIds = optionsIds;
    }
}
