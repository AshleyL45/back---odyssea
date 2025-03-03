package com.example.odyssea.dtos.UserItinerary;

import com.example.odyssea.dtos.ActivityDto;
import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.dtos.HotelDto;

import java.time.LocalDate;
import java.util.List;

public class UserItineraryDayDTO {

    private String cityName;
    private String countryName;
    private List<FlightItineraryDTO> flights;
    private List<HotelDto> hotels;
    private List<ActivityDto> activities;
    private int dayNumber;
    private LocalDate date;
    private boolean dayOff;


    public UserItineraryDayDTO() {
    }

    public UserItineraryDayDTO(String cityName, String countryName, List<FlightItineraryDTO> flights, List<HotelDto> hotels, List<ActivityDto> activities, int dayNumber, LocalDate date, boolean dayOff) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.flights = flights;
        this.hotels = hotels;
        this.activities = activities;
        this.dayNumber = dayNumber;
        this.date = date;
        this.dayOff = dayOff;
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

    public List<FlightItineraryDTO> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightItineraryDTO> flights) {
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

    public List<HotelDto> getHotels() {
        return hotels;
    }

    public void setHotels(List<HotelDto> hotels) {
        this.hotels = hotels;
    }

    public List<ActivityDto> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDto> activities) {
        this.activities = activities;
    }

    public boolean isDayOff() {
        return dayOff;
    }

    public void setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
    }
}
