package com.example.odyssea.dtos.UserItinerary;

import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.PlaneRide;
import com.example.odyssea.services.flight.PlaneRideService;

import java.time.LocalDate;

public class UserItineraryDayDTO {

    private String cityName;
    private String countryName;
    private HotelDto hotel;
    private Activity activity;
    private int dayNumber;
    private LocalDate date;
    private boolean dayOff;
    private PlaneRide planeRide;


    public UserItineraryDayDTO() {
    }

    public UserItineraryDayDTO(String cityName, String countryName, HotelDto hotel, Activity activity, int dayNumber, LocalDate date, boolean dayOff, PlaneRide planeRide) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.hotel = hotel;
        this.activity = activity;
        this.dayNumber = dayNumber;
        this.date = date;
        this.dayOff = dayOff;
        this.planeRide = planeRide;
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

    public HotelDto getHotel() {
        return hotel;
    }

    public void setHotel(HotelDto hotel) {
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

    public PlaneRide getPlaneRide() {
        return planeRide;
    }

    public void setPlaneRide(PlaneRide planeRide) {
        this.planeRide = planeRide;
    }
}
