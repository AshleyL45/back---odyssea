package com.example.odyssea.entities.userItinerary;

import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;


public class UserItinerary {
    @Id
    @Min(value = 1, message = "User Itinerary ID must be greater than or equal to 1")
    private int id;

    @Min(value = 1, message = "User ID must be greater than or equal to 1")
    private int userId;

    @NotNull(message = "Start date cannot be null.")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null.")
    private LocalDate endDate;

    @NotNull(message = "Starting price is required.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Starting price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Starting price must be a valid monetary amount with up to 2 decimal places")
    private BigDecimal startingPrice;

    @Min(value = 0, message = "Total duration must be greater than or equal to 0")
    private int totalDuration;

    @NotBlank(message = "Departure city is required.")
    @Size(max = 255, message = "Departure city must not exceed 255 characters")
    private String departureCity;

    @NotBlank(message = "Itinerary name is required.")
    @Size(max = 255, message = "Itinerary name must not exceed 255 characters")
    private String itineraryName;

    @Positive(message = "Number of adults must be positive.")
    private int numberOfAdults;

    @PositiveOrZero(message = "Number of kids must be zero or positive.")
    private int numberOfKids;

    @PositiveOrZero(message = "Flight ID cannot be negative.")
    private int flightId;

    @PositiveOrZero(message = "Option ID cannot be negative.")
    private int optionId;

    public UserItinerary() {
    }

    public UserItinerary(int id, int userId, LocalDate startDate, LocalDate endDate, BigDecimal startingPrice, int totalDuration, String departureCity, String itineraryName, int numberOfAdults, int numberOfKids) {
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
}
