package com.example.odyssea.dtos.mainTables;

public class ItineraryImageDto {
    private int imageId;
    private String link;
    private String altText;
    private String role;

    public ItineraryImageDto(int imageId, String link, String altText, String role) {
        this.imageId = imageId;
        this.link    = link;
        this.altText = altText;
        this.role    = role;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

