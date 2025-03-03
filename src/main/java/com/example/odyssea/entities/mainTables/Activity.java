package com.example.odyssea.entities.mainTables;

public class Activity {
    private int id;
    private int cityId;
    private String name;
    private String type;
    private String physicalEffort;
    private int duration;
    private String description;
    private Double price;

    public Activity() {}

    public Activity(int id, int cityId, String name, String type, String physicalEffort, int duration, String description, Double price) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.type = type;
        this.physicalEffort = physicalEffort;
        this.duration = duration;
        this.description = description;
        this.price = price;
    }

    // Getters et Setters
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPhysicalEffort() {
        return physicalEffort;
    }
    public void setPhysicalEffort(String physicalEffort) {
        this.physicalEffort = physicalEffort;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
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
}
