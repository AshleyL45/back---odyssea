package com.example.odyssea.dtos.Flight;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FlightDTO {
    private int id;
    private String companyName;
    private Duration duration;
    private LocalDateTime departureDateTime;
    private String departureCityIata;
    private LocalDateTime arrivalDateTime;
    private String arrivalCityIata;
    private BigDecimal price;
    private String airplaneName;

    public FlightDTO() {
    }

    public FlightDTO(int id, String companyName, Duration duration, LocalDateTime departureDateTime, String departureCityIata, LocalDateTime arrivalDateTime, String arrivalCityIata, BigDecimal price, String airplaneName) {
        this.id = id;
        this.companyName = companyName;
        this.duration = duration;
        this.departureDateTime = departureDateTime;
        this.departureCityIata = departureCityIata;
        this.arrivalDateTime = arrivalDateTime;
        this.arrivalCityIata = arrivalCityIata;
        this.price = price;
        this.airplaneName = airplaneName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getDepartureCityIata() {
        return departureCityIata;
    }

    public void setDepartureCityIata(String departureCityIata) {
        this.departureCityIata = departureCityIata;
    }

    public String getArrivalCityIata() {
        return arrivalCityIata;
    }

    public void setArrivalCityIata(String arrivalCityIata) {
        this.arrivalCityIata = arrivalCityIata;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAirplaneName() {
        return airplaneName;
    }

    public void setAirplaneName(String airplaneName) {
        this.airplaneName = airplaneName;
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
}
