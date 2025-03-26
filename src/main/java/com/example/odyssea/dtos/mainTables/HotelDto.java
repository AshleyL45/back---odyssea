package com.example.odyssea.dtos.mainTables;

import com.example.odyssea.entities.mainTables.Hotel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelDto {
    private Integer id;
    private int cityId;
    private String name;
    private int starRating;
    private String description;
    private double price;

    public HotelDto() {}

    public HotelDto(Integer id, int cityId, String name, int starRating, String description, double price) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.starRating = starRating;
        this.description = description;
        this.price = price;
    }

    public static HotelDto fromEntity(Hotel hotel) {
        return new HotelDto(
                hotel.getId(),
                hotel.getCityId(),
                hotel.getName(),
                hotel.getStarRating(),
                hotel.getDescription(),
                hotel.getPrice()
        );
    }


    public Hotel toEntity() {
        return new Hotel(
                id,
                cityId,
                name,
                starRating,
                description,
                price
        );
    }

  
    // Getters & Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
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
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
