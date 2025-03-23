package com.example.odyssea.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ItineraryThemes {
    private int id;
    private String name;
    private String description;
    private String shortDescription;
    private BigDecimal price;
    private int totalDuration;
    private int themeId;
    private String themeName;
    private String countriesVisited;

    public ItineraryThemes() {
    }

    public ItineraryThemes(int id, String name, String description, String shortDescription, BigDecimal price, int totalDuration, int themeId, String themeName, String countriesVisited) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.price = price;
        this.totalDuration = totalDuration;
        this.themeId = themeId;
        this.themeName = themeName;
        this.countriesVisited = countriesVisited;
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

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getCountriesVisited() {
        return countriesVisited;
    }

    public void setCountriesVisited(String countriesVisited) {
        this.countriesVisited = countriesVisited;
    }
}
