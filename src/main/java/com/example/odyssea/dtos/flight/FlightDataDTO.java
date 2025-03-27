package com.example.odyssea.dtos.flight;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FlightDataDTO {

    @JsonProperty("data")
    private List<FlightOffersDTO> data;
    @JsonProperty("dictionaries")
    private DictionnaryDTO dictionnary;

    public FlightDataDTO() {
    }

    public FlightDataDTO(List<FlightOffersDTO> data, DictionnaryDTO dictionnary) {
        this.data = data;
        this.dictionnary = dictionnary;
    }


    public List<FlightOffersDTO> getData() {
        return data;
    }

    public void setData(List<FlightOffersDTO> data) {
        this.data = data;
    }

    public DictionnaryDTO getDictionnary() {
        return dictionnary;
    }

    public void setDictionnary(DictionnaryDTO dictionnary) {
        this.dictionnary = dictionnary;
    }
}
