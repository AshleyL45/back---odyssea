package com.example.odyssea.entities.itinerary;

import java.math.BigDecimal;
import java.sql.Time;

public class Itinerary {


    private int idItinerary;
    private String itineraryName;
    private String description;
    private String shortDescription;
    private int stock;
    private BigDecimal price;
    private int totalDuration;
    private int themeId;



    public Itinerary(){}
    public Itinerary(int idItinerary, String itineraryName, String description, String shortDescription, int stock, BigDecimal price, int totalDuration, int themeId){
        this.idItinerary = idItinerary;
        this.itineraryName = itineraryName;
        this.description = description;
        this.shortDescription = shortDescription;
        this.stock = stock;
        this.price = price;
        this.totalDuration= totalDuration;
        this.themeId = themeId;
    }


    public int getIdItinerary() {
        return idItinerary;
    }

    public void setIdItinerary(int idItinerary) {
        this.idItinerary = idItinerary;
    }

    public String getItineraryName() {
        return itineraryName;
    }

    public void setItineraryName(String name) {
        this.itineraryName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String homeText) {
        this.shortDescription = homeText;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }
}
