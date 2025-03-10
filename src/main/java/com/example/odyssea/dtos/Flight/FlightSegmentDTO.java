package com.example.odyssea.dtos.Flight;

import com.example.odyssea.dtos.Flight.AirportDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightSegmentDTO { // Un seul vol
    @JsonProperty("departure")
    private AirportDTO departure;

    @JsonProperty("arrival")
    private AirportDTO arrival;

    @JsonProperty("carrierCode")
    private String carrierCode;

    @JsonProperty("aircraft")
    private AircraftDTO aircraftCode;

    @JsonProperty("duration")
    private String duration; // Durée d'un seul vol

    private String carrierName;
    private String aircraftName;

    public FlightSegmentDTO() {}

    public FlightSegmentDTO(AirportDTO departure, AirportDTO arrival, String carrierCode, AircraftDTO aircraftCode, String duration, String carrierName, String aircraftName) {
        this.departure = departure;
        this.arrival = arrival;
        this.carrierCode = carrierCode;
        this.aircraftCode = aircraftCode;
        this.duration = duration;
        this.carrierName = carrierName;
        this.aircraftName = aircraftName;
    }

    public AirportDTO getDeparture() { return departure; }
    public void setDeparture(AirportDTO departure) { this.departure = departure; }

    public AirportDTO getArrival() { return arrival; }
    public void setArrival(AirportDTO arrival) { this.arrival = arrival; }

    public String getCarrierCode() { return carrierCode; }
    public void setCarrierCode(String carrierCode) { this.carrierCode = carrierCode; }

    public AircraftDTO getAircraftCode() { return aircraftCode; }
    public void setAircraftCode(AircraftDTO aircraftCode) { this.aircraftCode = aircraftCode; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getAircraftName() {
        return aircraftName;
    }

    public void setAircraftName(String aircraftName) {
        this.aircraftName = aircraftName;
    }
}
