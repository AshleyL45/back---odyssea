package com.example.odyssea.entities.itinerary;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ItineraryStep {
    private int id;

    @Min(value = 1, message = "Itinerary ID must be greater than or equal to 1")
    private int itineraryId;

    @Min(value = 1, message = "City ID must be greater than or equal to 1")
    private int idCity;

    @Min(value = 1, message = "Country ID must be greater than or equal to 1")
    private int idCountry;

    @Min(value = 1, message = "Hotel ID must be greater than or equal to 1")
    private int idHotel;

    @Min(value = 1, message = "Position must be greater than or equal to 1")
    private int position;

    @Min(value = 1, message = "Day number must be greater than or equal to 1")
    private int dayNumber;

    @NotBlank(message = "Description per day is required")
    @Size(max = 5000, message = "Description per day must not exceed 5000 characters")
    private String descriptionPerDay;

    public ItineraryStep() {}

    public ItineraryStep(int id, int itineraryId, int idCity, int idCountry, int idHotel, int position, int dayNumber, String descriptionPerDay) {
        this.id = id;
        this.itineraryId = itineraryId;
        this.idCity = idCity;
        this.idCountry = idCountry;
        this.idHotel = idHotel;
        this.position = position;
        this.dayNumber = dayNumber;
        this.descriptionPerDay = descriptionPerDay;
    }

    public int getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
    }

    public int getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(int idCountry) {
        this.idCountry = idCountry;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getDescriptionPerDay() {
        return descriptionPerDay;
    }

    public void setDescriptionPerDay(String descriptionPerDay) {
        this.descriptionPerDay = descriptionPerDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
