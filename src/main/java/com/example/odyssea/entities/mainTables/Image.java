package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class Image {

    @Min(value = 1, message = "Image ID must be greater than or equal to 1")
    private int idImage;

    @Min(value = 1, message = "Itinerary ID must be greater than or equal to 1")
    private int itineraryId;

    @NotBlank(message = "Size type is required")
    @Size(max = 100, message = "Size type must not exceed 100 characters")
    private String sizeType;

    @NotBlank(message = "Link is required")
    @Size(max = 500, message = "Link must not exceed 500 characters")
    private String link;

    @NotBlank(message = "Alt text is required")
    @Size(max = 255, message = "Alt text must not exceed 255 characters")
    private String altText;

    public Image() {}

    public Image(int idImage,
                 int itineraryId,
                 String sizeType,
                 String link,
                 String altText) {
        this.idImage = idImage;
        this.itineraryId = itineraryId;
        this.sizeType = sizeType;
        this.link = link;
        this.altText = altText;
    }

    public int getIdImage() { return idImage; }
    public void setIdImage(int idImage) { this.idImage = idImage; }

    public int getItineraryId() { return itineraryId; }
    public void setItineraryId(int itineraryId) { this.itineraryId = itineraryId; }

    public String getSizeType() { return sizeType; }
    public void setSizeType(String sizeType) { this.sizeType = sizeType; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }
}
