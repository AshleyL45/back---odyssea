package com.example.odyssea.dtos.mainTables;

public class ItineraryImageDto {
    private int itineraryId;
    private int imageId;
    private String role;

    public ItineraryImageDto() {}

    public ItineraryImageDto(int itineraryId, int imageId, String role) {
        this.itineraryId = itineraryId;
        this.imageId = imageId;
        this.role = role;
    }

    public int getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}