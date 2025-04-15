package com.example.odyssea.dtos.userItinerary;

import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.entities.mainTables.Country;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.userItinerary.drafts.UserItineraryDraft;

import java.util.List;

public class DraftData {
    UserItineraryDraft draft;
    List<Country> countries;
    List<City> cities;
    List<Activity> activities;
    List<Option> options;

    public DraftData() {
    }

    public DraftData(UserItineraryDraft draft, List<Country> countries, List<City> cities, List<Activity> activities, List<Option> options) {
        this.draft = draft;
        this.countries = countries;
        this.cities = cities;
        this.activities = activities;
        this.options = options;
    }


    public UserItineraryDraft getDraft() {
        return draft;
    }

    public void setDraft(UserItineraryDraft draft) {
        this.draft = draft;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
