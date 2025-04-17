package com.example.odyssea.dtos.userItinerary;

import com.example.odyssea.dtos.flight.FlightItineraryDTO;
import com.example.odyssea.dtos.mainTables.HotelDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.Hotel;

import java.time.LocalDate;

public class UserItineraryDayDTO {

    private String cityName;
    private String countryName;
    private Hotel hotel;
    private Activity activity;
    private int dayNumber;
    private LocalDate date;
    private boolean dayOff;
    private FlightItineraryDTO flightItineraryDTO;


    public UserItineraryDayDTO() {
    }

    public UserItineraryDayDTO(String cityName, String countryName, Hotel hotel, Activity activity, int dayNumber, LocalDate date, boolean dayOff, FlightItineraryDTO flightItineraryDTO) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.hotel = hotel;
        this.activity = activity;
        this.dayNumber = dayNumber;
        this.date = date;
        this.dayOff = dayOff;
        this.flightItineraryDTO = flightItineraryDTO;
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

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isDayOff() {
        return dayOff;
    }

    public void setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
    }

    public FlightItineraryDTO getFlightItineraryDTO() {
        return flightItineraryDTO;
    }

    public void setFlightItineraryDTO(FlightItineraryDTO flightItineraryDTO) {
        this.flightItineraryDTO = flightItineraryDTO;
    }
}
