package com.example.odyssea.dtos.UserItinerary;

import com.example.odyssea.dtos.Flight.ItineraryDTO;

import java.time.LocalDate;

public class UserItineraryDayDTO {

    private String cityName;
    private String countryName;
    private ItineraryDTO flights;
    //ActivityDTO et HôtelDTO
    private int dayNumber;
    private LocalDate date;


    public UserItineraryDayDTO() {
    }

    public UserItineraryDayDTO(String cityName, String countryName, ItineraryDTO flights, int dayNumber, LocalDate date) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.flights = flights;
        this.dayNumber = dayNumber;
        this.date = date;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public ItineraryDTO getFlights() {
        return flights;
    }

    public void setFlights(ItineraryDTO flights) {
        this.flights = flights;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
