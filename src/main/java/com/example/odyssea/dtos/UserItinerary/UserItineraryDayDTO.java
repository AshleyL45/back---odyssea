package com.example.odyssea.dtos.UserItinerary;

import com.example.odyssea.dtos.Flight.FlightItineraryDTO;

import java.time.LocalDate;

public class UserItineraryDayDTO {

    private String cityName;
    private String countryName;
    private FlightItineraryDTO flights;
    //ActivityDTO et HôtelDTO
    private int dayNumber;
    private LocalDate date;


    public UserItineraryDayDTO() {
    }

    public UserItineraryDayDTO(String cityName, String countryName, FlightItineraryDTO flights, int dayNumber, LocalDate date) {
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

    public FlightItineraryDTO getFlights() {
        return flights;
    }

    public void setFlights(FlightItineraryDTO flights) {
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
