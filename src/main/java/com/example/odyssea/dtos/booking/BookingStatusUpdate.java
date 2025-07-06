package com.example.odyssea.dtos.booking;

import com.example.odyssea.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;

public class BookingStatusUpdate {

    @NotNull(message = "Booking status must not be null")
    private BookingStatus newStatus;

    public BookingStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(BookingStatus newStatus) {
        this.newStatus = newStatus;
    }


}
