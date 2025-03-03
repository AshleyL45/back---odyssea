package com.example.odyssea.entities.mainTables;

public class Country {
    private int id;
    private String name;
    private String continent;
    private double price;

    public Country() {}

    public Country(int id, String name, String continent, double price) {
        this.id = id;
        this.name = name;
        this.continent = continent;
        this.price = price;
    }

    // Getters et Setters
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
    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.continent = continent;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
