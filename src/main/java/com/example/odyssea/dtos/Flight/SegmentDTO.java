package com.example.odyssea.dtos.Flight;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SegmentDTO {

    @JsonProperty("departure")
    private AirportDTO departure;

    @JsonProperty("arrival")
    private AirportDTO arrival;

    @JsonProperty("carrierCode")
    private String carrierCode;

    @JsonProperty("aircraft")
    private AircraftDTO aircraft;

    @JsonProperty("duration")
    private String duration;

    public SegmentDTO() {
    }

    public SegmentDTO(AirportDTO departure, AirportDTO arrival, String carrierCode, AircraftDTO aircraft, String duration) {
        this.departure = departure;
        this.arrival = arrival;
        this.carrierCode = carrierCode;
        this.aircraft = aircraft;
        this.duration = duration;
    }

    public AirportDTO getDeparture() {
        return departure;
    }

    public void setDeparture(AirportDTO departure) {
        this.departure = departure;
    }

    public AirportDTO getArrival() {
        return arrival;
    }

    public void setArrival(AirportDTO arrival) {
        this.arrival = arrival;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public AircraftDTO getAircraft() {
        return aircraft;
    }

    public void setAircraft(AircraftDTO aircraft) {
        this.aircraft = aircraft;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
