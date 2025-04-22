package com.example.odyssea.dtos.draft;

import io.swagger.v3.oas.annotations.media.Schema;

public class DepartureCity {
    @Schema(example = "London")
    private String departureCity;

    public DepartureCity() {
    }

    public DepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }
}
