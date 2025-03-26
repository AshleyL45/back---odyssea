package com.example.odyssea.dtos.flight;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationDTO {
    @JsonProperty("cityCode")
    private String cityCode;

    @JsonProperty("countryCode")
    private String countryCode;

    public LocationDTO() {
    }

    public LocationDTO(String cityCode, String countryCode) {
        this.cityCode = cityCode;
        this.countryCode = countryCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
