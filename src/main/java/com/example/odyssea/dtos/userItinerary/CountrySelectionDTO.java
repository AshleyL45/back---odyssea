package com.example.odyssea.dtos.userItinerary;

import java.util.List;

public class CountrySelectionDTO {
    private String countryName;
    private List<CitySelectionDTO> citySelection;


    public CountrySelectionDTO() {
    }

    public CountrySelectionDTO(String countryName, List<CitySelectionDTO> citySelection) {
        this.countryName = countryName;
        this.citySelection = citySelection;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<CitySelectionDTO> getCitySelection() {
        return citySelection;
    }

    public void setCitySelection(List<CitySelectionDTO> citySelection) {
        this.citySelection = citySelection;
    }
}
