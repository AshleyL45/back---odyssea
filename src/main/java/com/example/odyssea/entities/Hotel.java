package com.example.odyssea.entities;

public class Hotel {

    private int id;
    private int cityId;
    private String name;
    private int starRating;
    private String description;
    private Double price;

    public Hotel() {}

    public Hotel(int id, int cityId, String name, int starRating, String description, Double price) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.starRating = starRating;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * 🔹 Convertit la réponse de l'API Amadeus en objet `Hotel`
     */
    public static Hotel fromApiResponse(String hotelId, int cityId, String name, int starRating, String description, double price) {
        return new Hotel(
                Integer.parseInt(hotelId.replaceAll("\\D", "")), // Convertit l'ID en entier en supprimant les lettres éventuelles
                cityId,
                name,
                starRating,
                description,
                price
        );
    }
}
