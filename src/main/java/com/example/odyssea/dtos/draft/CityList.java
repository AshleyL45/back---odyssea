package com.example.odyssea.dtos.draft;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class CityList {
    @Schema(example = "[2, 3]")
    private List<Integer> cities;

    public CityList() {
    }

    public CityList(List<Integer> cities) {
        this.cities = cities;
    }

    public List<Integer> getCities() {
        return cities;
    }

    public void setCities(List<Integer> cities) {
        this.cities = cities;
    }
}
