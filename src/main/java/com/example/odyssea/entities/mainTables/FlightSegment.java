package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.Duration;

public class FlightSegment {

    @Min(value = 1, message = "Flight segment ID must be greater than or equal to 1")
    private int id;

    @NotBlank(message = "Departure airport IATA code is required")
    @Size(max = 10, message = "Departure airport IATA code must not exceed 10 characters")
    private String departureAirportIata;

    @NotBlank(message = "Arrival airport IATA code is required")
    @Size(max = 10, message = "Arrival airport IATA code must not exceed 10 characters")
    private String arrivalAirportIata;

    @NotNull(message = "Departure date and time is required")
    private LocalDateTime departureDateTime;

    @NotNull(message = "Arrival date and time is required")
    private LocalDateTime arrivalDateTime;

    @NotBlank(message = "Carrier code is required")
    @Size(max = 10, message = "Carrier code must not exceed 10 characters")
    private String carrierCode;

    @NotBlank(message = "Carrier name is required")
    @Size(max = 255, message = "Carrier name must not exceed 255 characters")
    private String carrierName;

    @NotBlank(message = "Aircraft code is required")
    @Size(max = 10, message = "Aircraft code must not exceed 10 characters")
    private String aircraftCode;

    @NotBlank(message = "Aircraft name is required")
    @Size(max = 255, message = "Aircraft name must not exceed 255 characters")
    private String aircraftName;

    @NotNull(message = "Duration is required")
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

    // Getters and Setters

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
