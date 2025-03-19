package com.example.odyssea.entities.userItinerary;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.sql.Date;


public class UserItinerary {
    @Id
    private int id;
    private int userId;
    private Date startDate;
    private Date endDate;
    private BigDecimal startingPrice;
    private int totalDuration;
    private String departureCity;
    private String itineraryName;
    private int numberOfAdults;
    private int numberOfKids;
//    private int flightId;
    //private int optionId;



    public UserItinerary() {
    }

    public UserItinerary(int id, int userId, Date startDate, Date endDate, BigDecimal startingPrice, int totalDuration, String departureCity, String itineraryName, int numberOfAdults, int numberOfKids) {
        this.id = id;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startingPrice = startingPrice;
        this.totalDuration = totalDuration;
        this.departureCity = departureCity;
        this.itineraryName = itineraryName;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
       // this.flightId = flightId;
       // this.optionId = optionId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getItineraryName() {
        return itineraryName;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfKids() {
        return numberOfKids;
    }

    public void setNumberOfKids(int numberOfKids) {
        this.numberOfKids = numberOfKids;
    }

//    public int getPlaneRideId() {
//        return flightId;
//    }
//
//    public void setPlaneRideId(int flightId) {
//        this.flightId = flightId;
//    }

//    public int getOptionId() {
//        return optionId;
//    }
//
//    public void setOptionId(int optionId) {
//        this.optionId = optionId;
//    }
}
