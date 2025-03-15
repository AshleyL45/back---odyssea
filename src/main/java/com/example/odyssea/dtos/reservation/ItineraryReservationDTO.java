package com.example.odyssea.dtos.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ItineraryReservationDTO {
        private int id;
        private String itineraryName;
        private String description;
        private String shortDescription;
        private BigDecimal price;
        private int duration;
        private String status;

        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate purchaseDate;

    public ItineraryReservationDTO() {
    }

    public ItineraryReservationDTO(int id, String itineraryName, String description, String shortDescription, BigDecimal price, int duration, String status, LocalDate purchaseDate) {
        this.id = id;
        this.itineraryName = itineraryName;
        this.description = description;
        this.shortDescription = shortDescription;
        this.price = price;
        this.duration = duration;
        this.status = status;
        this.purchaseDate = purchaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItineraryName() {
        return itineraryName;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
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

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

}
