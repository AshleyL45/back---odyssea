package com.example.odyssea.dtos;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class FlightDTO {
    private int id;
    private String companyName;
    private Duration duration;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private String departureCityIata;
    private LocalDate arrivalDate;
    private LocalTime arrivalTime;
    private String arrivalCityIata;
    private BigDecimal price;
    private String airplaneName;

    public FlightDTO() {
    }

    public FlightDTO(int id, String companyName, Duration duration, LocalDate departureDate, LocalTime departureTime, String departureCityIata, LocalDate arrivalDate, LocalTime arrivalTime, String arrivalCityIata, BigDecimal price, String airplaneName) {
        this.id = id;
        this.companyName = companyName;
        this.duration = duration;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.departureCityIata = departureCityIata;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
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

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureCityIata() {
        return departureCityIata;
    }

    public void setDepartureCityIata(String departureCityIata) {
        this.departureCityIata = departureCityIata;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
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
}
