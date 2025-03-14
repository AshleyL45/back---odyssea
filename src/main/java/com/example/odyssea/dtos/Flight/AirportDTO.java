package com.example.odyssea.dtos.Flight;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AirportDTO {

    @JsonProperty("iataCode")
    private String iataCode;

    @JsonProperty("at")
    private LocalDateTime dateTime;

    public AirportDTO(String iataCode, LocalDateTime dateTime) {
        this.iataCode = iataCode;
        this.dateTime = dateTime;
    }

    public AirportDTO() {
    }


    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
