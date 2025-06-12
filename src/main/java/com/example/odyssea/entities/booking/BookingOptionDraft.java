package com.example.odyssea.entities.booking;

public class BookingOptionDraft {
    private int booking;
    private int optionId;


    public BookingOptionDraft() {
    }

    public BookingOptionDraft(int booking, int optionId) {
        this.booking = booking;
        this.optionId = optionId;
    }

    public int getBooking() {
        return booking;
    }

    public void setBooking(int booking) {
        this.booking = booking;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
}

