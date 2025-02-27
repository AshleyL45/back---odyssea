package com.example.odyssea.entities.itinerary;

public class ItineraryStep {

    private int idItineraryStep;
    private int idItinerary;
    private int idCity;
    private int idCountry;
    private int idHotel;
    private int position;
    private int dayNumber;
    private String descriptionPerDay;


    public ItineraryStep(){}
    public ItineraryStep(int idItineraryStep, int idItinerary, int idCity, int idCountry, int idHotel, int position, int dayNumber, String descriptionPerDay){
        this.idItineraryStep = idItineraryStep;
        this.idItinerary = idItinerary;
        this.idCity = idCity;
        this.idCountry = idCountry;
        this.idHotel = idHotel;
        this.position = position;
        this.dayNumber = dayNumber;
        this.descriptionPerDay = descriptionPerDay;
    }


    public int getIdItineraryStep() {
        return idItineraryStep;
    }

    public void setIdItineraryStep(int idItineraryStep) {
        this.idItineraryStep = idItineraryStep;
    }

    public int getIdItinerary() {
        return idItinerary;
    }

    public void setIdItinerary(int idItinerary) {
        this.idItinerary = idItinerary;
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
}
