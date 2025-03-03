package com.example.odyssea.dtos.Flight;

import com.example.odyssea.dtos.Flight.AirportDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightSegmentDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("departure")
    private AirportDTO departure;

    @JsonProperty("arrival")
    private AirportDTO arrival;

    @JsonProperty("carrierCode")
    private String carrierCode;

    @JsonProperty("aircraft")
    private AircraftDTO aircraftCode;

    @JsonProperty("duration")
    private String duration;

    public FlightSegmentDTO() {}

    public FlightSegmentDTO(String id, AirportDTO departure, AirportDTO arrival, String carrierCode, AircraftDTO aircraftCode, String duration) {
        this.id = id;
        this.departure = departure;
        this.arrival = arrival;
        this.carrierCode = carrierCode;
        this.aircraftCode = aircraftCode;
        this.duration = duration;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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
}
