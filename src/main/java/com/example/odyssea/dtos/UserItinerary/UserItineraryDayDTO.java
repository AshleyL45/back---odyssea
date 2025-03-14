package com.example.odyssea.dtos.UserItinerary;

import com.example.odyssea.daos.userItinerary.UserItineraryStepDao;
import com.example.odyssea.dtos.ActivityDto;
import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class UserItineraryDayDTO {

    private String cityName;
    private String countryName;
    private List<HotelDto> hotels;
    private List<Activity> activities;
    private int dayNumber;
    private LocalDate date;
    private boolean dayOff;


    public UserItineraryDayDTO() {
    }

    public UserItineraryDayDTO(String cityName, String countryName, List<HotelDto> hotels, List<Activity> activities, int dayNumber, LocalDate date, boolean dayOff) {
        this.cityName = cityName;
        this.countryName = countryName;
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

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public boolean isDayOff() {
        return dayOff;
    }

    public void setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
    }
}
