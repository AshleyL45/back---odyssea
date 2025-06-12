package com.example.odyssea.dtos.booking;

import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.entities.booking.Booking;
import com.example.odyssea.entities.mainTables.Option;

import java.time.LocalDate;
import java.util.List;

public class BookingConfirmation {

    private Itinerary itinerary;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private String status;
    private int numberOfAdults;
    private int numberOfKids;
    private List<Option> optionList;
    private LocalDate purchaseDate;


    public BookingConfirmation() {}

    public BookingConfirmation(Itinerary itinerary, LocalDate departureDate, LocalDate returnDate,
                               String status, int numberOfAdults, int numberOfKids,
                               List<Option> optionList, LocalDate purchaseDate) {
        this.itinerary = itinerary;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.status = status;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
        this.optionList = optionList;
        this.purchaseDate = purchaseDate;
    }

    // Getters et Setters
    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public static BookingConfirmation fromEntities(
            Booking booking,
            Itinerary itinerary,
            List<Option> options
    ) {

        return new BookingConfirmation(
                itinerary,
                booking.getDepartureDate(),
                booking.getReturnDate(),
                booking.getStatus(),
                booking.getNumberOfAdults(),
                booking.getNumberOfKids(),
                options,
                booking.getPurchaseDate()
        );
    }
}
