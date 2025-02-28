package com.example.odyssea.dtos.Flight;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class FlightOffersDTO {

    @JsonProperty("itineraries")
    private List<ItineraryDTO> itineraries;  // Aller et retour

    @JsonProperty("price")
    private FlightPriceDTO price;


    public FlightOffersDTO() {
    }

    public FlightOffersDTO(List<ItineraryDTO> itineraries, FlightPriceDTO price) {
        this.itineraries = itineraries;
        this.price = price;
    }

    public List<ItineraryDTO> getItineraries() {
        return itineraries;
    }

    public void setItineraries(List<ItineraryDTO> itineraries) {
        this.itineraries = itineraries;
    }

    public FlightPriceDTO getPrice() {
        return price;
    }

    public void setPrice(FlightPriceDTO price) {
        this.price = price;
    }
}
