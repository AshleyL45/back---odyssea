package com.example.odyssea.dtos.Flight;

import com.example.odyssea.dtos.Flight.AircraftDTO;
import com.example.odyssea.dtos.Flight.AirportDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

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

    // Champ ajouté pour le prix du segment (non persistant)
    @JsonProperty("price")
    private BigDecimal price;

    // Nouveaux champs pour le nom du transporteur et de l'avion
    @JsonProperty("carrierName")
    private String carrierName;

    @JsonProperty("aircraftName")
    private String aircraftName;

    public FlightSegmentDTO() {}

    public FlightSegmentDTO(String id, AirportDTO departure, AirportDTO arrival, String carrierCode, AircraftDTO aircraftCode, String duration, BigDecimal price, String carrierName, String aircraftName) {
        this.id = id;
        this.departure = departure;
        this.arrival = arrival;
        this.carrierCode = carrierCode;
        this.aircraftCode = aircraftCode;
        this.duration = duration;
        this.price = price;
        this.carrierName = carrierName;
        this.aircraftName = aircraftName;
    }

    // Getters et setters pour tous les champs

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

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCarrierName() { return carrierName; }
    public void setCarrierName(String carrierName) { this.carrierName = carrierName; }

    public String getAircraftName() { return aircraftName; }
    public void setAircraftName(String aircraftName) { this.aircraftName = aircraftName; }
}
