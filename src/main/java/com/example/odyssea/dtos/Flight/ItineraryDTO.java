package com.example.odyssea.dtos.Flight;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ItineraryDTO {

    private FlightDTO outboundFlight;// Vol aller
    private FlightDTO returnFlight; // Vol retour
    private BigDecimal totalPrice;

    public ItineraryDTO() {
    }


    public ItineraryDTO(FlightDTO outboundFlight, FlightDTO returnFlight, BigDecimal totalPrice) {
        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.totalPrice = totalPrice;
    }

    public FlightDTO getOutboundFlight() {
        return outboundFlight;
    }

    public void setOutboundFlight(FlightDTO outboundFlight) {
        this.outboundFlight = outboundFlight;
    }

    public FlightDTO getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(FlightDTO returnFlight) {
        this.returnFlight = returnFlight;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
