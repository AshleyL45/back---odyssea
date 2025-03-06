package com.example.odyssea.dtos.UserItinerary;

import com.example.odyssea.entities.userItinerary.UserItinerary;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserItineraryDTO {
    private int userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int duration;
    private String departureCity;
    private BigDecimal startingPrice;
    private String itineraryName;
    private int numberOfAdults;
    private int numberOfKids;
    private List<UserItineraryDayDTO> itineraryDays;

    public UserItineraryDTO(int userId, LocalDate startDate, LocalDate endDate, int duration, String departureCity, BigDecimal startingPrice, String itineraryName, int numberOfAdults, int numberOfKids, List<UserItineraryDayDTO> itineraryDays) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.departureCity = departureCity;
        this.startingPrice = startingPrice;
        this.itineraryName = itineraryName;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
        this.itineraryDays = itineraryDays;
    }

    public UserItineraryDTO() {
    }


    private static LocalDate convertToLocalDate(Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }


    public static UserItineraryDTO toUserItineraryEntity (UserItinerary userItinerary){
        LocalDate startDate = convertToLocalDate(userItinerary.getStartDate());
        LocalDate endDate = convertToLocalDate(userItinerary.getEndDate()) ;

        return new UserItineraryDTO(
                userItinerary.getUserId(),
                startDate,
                endDate,
                userItinerary.getTotalDuration(),
                userItinerary.getDepartureCity(),
                userItinerary.getStartingPrice(),
                userItinerary.getItineraryName(),
                userItinerary.getNumberOfAdults(),
                userItinerary.getNumberOfKids(),
                new ArrayList<>()
        );

    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
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

    public List<UserItineraryDayDTO> getItineraryDays() {
        return itineraryDays;
    }

    public void setItineraryDays(List<UserItineraryDayDTO> itineraryDays) {
        this.itineraryDays = itineraryDays;
    }
}
