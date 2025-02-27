package com.example.odyssea.entities.itinerary;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.DecimalFormat;

public class Itinerary {


    private int idItinerary;
    private String itineraryName;
    private String description;
    private String homeText;
    private int stock;
    private BigDecimal price;
    private Time totalDuration;



    public Itinerary(){}
    public Itinerary(int idItinerary, String itineraryName, String description, String homeText, int stock, BigDecimal price, Time totalDuration){
        this.idItinerary = idItinerary;
        this.itineraryName = itineraryName;
        this.description = description;
        this.homeText = homeText;
        this.stock = stock;
        this.price = price;
        this.totalDuration= totalDuration;
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


    public String getHomeText() {
        return homeText;
    }

    public void setHomeText(String homeText) {
        this.homeText = homeText;
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

    public Time getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Time totalDuration) {
        this.totalDuration = totalDuration;
    }
}
