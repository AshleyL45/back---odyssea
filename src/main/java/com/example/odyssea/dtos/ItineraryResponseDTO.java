package com.example.odyssea.dtos;

import java.math.BigDecimal;
import java.util.List;

public class ItineraryResponseDTO {
    private int id;
    private String name;
    private String description;
    private String shortDescription;
    private int stock;
    private BigDecimal price;
    private int totalDuration;
    private String themeName;
    private List<DailyPlanDto> days;

    public ItineraryResponseDTO() {
    }

    public ItineraryResponseDTO(int id, String name, String description, String shortDescription, int stock, BigDecimal price, int totalDuration, String themeName, List<DailyPlanDto> days) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.stock = stock;
        this.price = price;
        this.totalDuration = totalDuration;
        this.themeName = themeName;
        this.days = days;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public List<DailyPlanDto> getDays() {
        return days;
    }

    public void setDays(List<DailyPlanDto> days) {
        this.days = days;
    }
}
