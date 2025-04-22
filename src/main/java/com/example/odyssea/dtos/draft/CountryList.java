package com.example.odyssea.dtos.draft;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class CountryList {
    @Schema(example = "[55, 102]")
    private List<Integer> countries;


    public CountryList() {
    }

    public CountryList(List<Integer> countries) {
        this.countries = countries;
    }

    public List<Integer> getCountries() {
        return countries;
    }

    public void setCountries(List<Integer> countries) {
        this.countries = countries;
    }
}
