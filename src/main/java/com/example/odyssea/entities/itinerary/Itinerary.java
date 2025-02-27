package com.example.odyssea.entities.itinerary;

public class Itinerary {


    private int idItinerary;
    private String itineraryName;
    private String description;
    private String homeText;
    private int stock;
    private Double price;
    private int totalDuration;



    public Itinerary(){}
    public Itinerary(int idItinerary, String itineraryName, String description, String homeText, int stock, Double price, int totalDuration){
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }
}
