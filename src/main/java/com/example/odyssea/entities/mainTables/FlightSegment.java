package com.example.odyssea.entities.mainTables;

import java.time.LocalDateTime;
import java.time.Duration;

public class FlightSegment {
    private int id;
    private String departureAirportIata;
    private String arrivalAirportIata;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private String carrierCode;
    private String carrierName;
    private String aircraftCode;
    private String aircraftName;
    private Duration duration;

    public FlightSegment() {}

    public FlightSegment(int id, String departureAirportIata, String arrivalAirportIata,
                         LocalDateTime departureDateTime, LocalDateTime arrivalDateTime,
                         String carrierCode, String carrierName,
                         String aircraftCode, String aircraftName, Duration duration) {
        this.id = id;
        this.departureAirportIata = departureAirportIata;
        this.arrivalAirportIata = arrivalAirportIata;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.carrierCode = carrierCode;
        this.carrierName = carrierName;
        this.aircraftCode = aircraftCode;
        this.aircraftName = aircraftName;
        this.duration = duration;
    }

    // Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartureAirportIata() {
        return departureAirportIata;
    }

    public void setDepartureAirportIata(String departureAirportIata) {
        this.departureAirportIata = departureAirportIata;
    }

    public String getArrivalAirportIata() {
        return arrivalAirportIata;
    }

    public void setArrivalAirportIata(String arrivalAirportIata) {
        this.arrivalAirportIata = arrivalAirportIata;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }

    public String getAircraftName() {
        return aircraftName;
    }

    public void setAircraftName(String aircraftName) {
        this.aircraftName = aircraftName;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
