package com.example.odyssea.dtos.Flight;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FlightDataDTO {

    @JsonProperty("data")
    private List<FlightOffersDTO> data;
    @JsonProperty("dictionaries")
    private DictionnaryDTO dictionnary;

    public FlightDataDTO() {
    }

    public FlightDataDTO(List<FlightOffersDTO> flightOffers, DictionnaryDTO dictionnary) {
        this.data = flightOffers;
        this.dictionnary = dictionnary;
    }


    public List<FlightOffersDTO> getFlightOffers() {
        return data;
    }

    public void setFlightOffers(List<FlightOffersDTO> flightOffers) {
        this.data = this.data;
    }

    public DictionnaryDTO getDictionnary() {
        return dictionnary;
    }

    public void setDictionnary(DictionnaryDTO dictionnary) {
        this.dictionnary = dictionnary;
    }
}
