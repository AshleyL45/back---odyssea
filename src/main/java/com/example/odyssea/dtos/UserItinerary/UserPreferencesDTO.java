package com.example.odyssea.dtos.UserItinerary;

import com.example.odyssea.entities.mainTables.Option;

import java.time.LocalDate;
import java.util.List;

public class UserPreferencesDTO {
    private int userId;
    private LocalDate startDate;
    private String departureCity;
    private List<CountrySelectionDTO>  countrySelection;
    private int numberOfAdults;
    private int numberOfKids;
    private int hotelStanding;
    private List<Option> options;
    private String itineraryName;

    public UserPreferencesDTO() {
    }

    public UserPreferencesDTO(int userId, LocalDate startDate, String departureCity, List<CountrySelectionDTO> countrySelection, int numberOfAdults, int numberOfKids, int hotelStanding, List<Option> options, String itineraryName) {
        this.userId = userId;
        this.startDate = startDate;
        this.departureCity = departureCity;
        this.countrySelection = countrySelection;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
        this.hotelStanding = hotelStanding;
        this.options = options;
        this.itineraryName = itineraryName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public List<CountrySelectionDTO> getCountrySelection() {
        return countrySelection;
    }

    public void setCountrySelection(List<CountrySelectionDTO> countrySelection) {
        this.countrySelection = countrySelection;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfKids() {
        return numberOfKids;
    }

    public void setNumberOfKids(int numberOfKids) {
        this.numberOfKids = numberOfKids;
    }

    public int getHotelStanding() {
        return hotelStanding;
    }

    public void setHotelStanding(int hotelStanding) {
        this.hotelStanding = hotelStanding;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getItineraryName() {
        return itineraryName;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }
    // Activities per city


}
