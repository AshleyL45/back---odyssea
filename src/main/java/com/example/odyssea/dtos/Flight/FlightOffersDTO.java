package com.example.odyssea.dtos.Flight;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class FlightOffersDTO {

    @JsonProperty("oneWay")
    private boolean oneWay;
    @JsonProperty("itineraries")

    private List<FlightItineraryDTO> itineraries;  // Offre de vol

    @JsonProperty("price")
    private FlightPriceDTO price;


    public FlightOffersDTO() {
    }

    public FlightOffersDTO(boolean oneWay, List<FlightItineraryDTO> itineraries, FlightPriceDTO price) {
        this.oneWay = oneWay;
        this.itineraries = itineraries;
        this.price = price;
    }

    public List<FlightItineraryDTO> getItineraries() {
        return itineraries;
    }

    public void setItineraries(List<FlightItineraryDTO> itineraries) {
        this.itineraries = itineraries;
    }

    public FlightPriceDTO getPrice() {
        return price;
    }

    public void setPrice(FlightPriceDTO price) {
        this.price = price;
    }

    public boolean isOneWay() {
        return oneWay;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }
}
