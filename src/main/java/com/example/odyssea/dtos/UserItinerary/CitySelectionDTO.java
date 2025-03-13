package com.example.odyssea.dtos.UserItinerary;

import com.example.odyssea.dtos.ActivityDto;
import com.example.odyssea.entities.mainTables.Activity;

import java.util.List;

public class CitySelectionDTO {
    private String cityName;
    private List<Activity> activities;


    public CitySelectionDTO() {
    }

    public CitySelectionDTO(String cityName, List<Activity> activities) {
        this.cityName = cityName;
        this.activities = activities;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
