package com.example.odyssea.dtos.reservation;

import com.example.odyssea.dtos.userItinerary.UserItineraryDTO;
import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AdminUserItineraryDetails {
    private int id;
    private String userFirstName;
    private String userLastName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalDuration;
    private String departureCity;
    private BigDecimal startingPrice;
    private String itineraryName;
    private int numberOfAdults;
    private int numberOfKids;
    private List<UserItineraryDayDTO> itineraryDays;
    private List<Option> options;
    private LocalDate bookingDate;
    private BookingStatus status;

    public AdminUserItineraryDetails() {
    }

    public AdminUserItineraryDetails(int id, String userFirstName, String userLastName, LocalDate startDate, LocalDate endDate, int totalDuration, String departureCity, BigDecimal startingPrice, String itineraryName, int numberOfAdults, int numberOfKids, List<UserItineraryDayDTO> itineraryDays, List<Option> options, LocalDate bookingDate, BookingStatus status) {
        this.id = id;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDuration = totalDuration;
        this.departureCity = departureCity;
        this.startingPrice = startingPrice;
        this.itineraryName = itineraryName;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
        this.itineraryDays = itineraryDays;
        this.options = options;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
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

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public static AdminUserItineraryDetails fromEntities(User user, UserItineraryDTO itinerary){
        return new AdminUserItineraryDetails(
                itinerary.getId(),
                user.getFirstName(),
                user.getLastName(),
                itinerary.getStartDate(),
                itinerary.getEndDate(),
                itinerary.getTotalDuration(),
                itinerary.getDepartureCity(),
                itinerary.getStartingPrice(),
                itinerary.getItineraryName(),
                itinerary.getNumberOfAdults(),
                itinerary.getNumberOfKids(),
                itinerary.getItineraryDays(),
                itinerary.getOptions(),
                itinerary.getBookingDate(),
                itinerary.getStatus()
        );
    }
}
