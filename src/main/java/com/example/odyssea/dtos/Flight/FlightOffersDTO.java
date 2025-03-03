package com.example.odyssea.dtos.Flight;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class FlightOffersDTO {

    @JsonProperty("itineraries")
    private List<FlightItineraryDTO> itineraries;  // Aller et retour

    @JsonProperty("price")
    private FlightPriceDTO price;


    public FlightOffersDTO() {
    }

    public FlightOffersDTO(List<FlightItineraryDTO> itineraries, FlightPriceDTO price) {
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
}
