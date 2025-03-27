package com.example.odyssea.dtos.flight;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class DictionnaryDTO {
    @JsonProperty("locations")
    private Map<String, LocationDTO> locations;

    @JsonProperty("aircraft")
    private Map<String, String> aircraft;

    @JsonProperty("currencies")
    private Map<String, String> currencies;

    @JsonProperty("carriers")
    private Map<String, String> carriers;

    public DictionnaryDTO() {
    }

    public DictionnaryDTO(Map<String, LocationDTO> locations, Map<String, String> aircraft, Map<String, String> currencies, Map<String, String> carriers) {
        this.locations = locations;
        this.aircraft = aircraft;
        this.currencies = currencies;
        this.carriers = carriers;
    }

    public Map<String, LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, LocationDTO> locations) {
        this.locations = locations;
    }

    public Map<String, String> getAircraft() {
        return aircraft;
    }

    public void setAircraft(Map<String, String> aircraft) {
        this.aircraft = aircraft;
    }

    public Map<String, String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, String> currencies) {
        this.currencies = currencies;
    }

    public Map<String, String> getCarriers() {
        return carriers;
    }

    public void setCarriers(Map<String, String> carriers) {
        this.carriers = carriers;
    }
}
