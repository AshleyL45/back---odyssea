// src/main/java/com/example/odyssea/services/booking/BookingOptionService.java
package com.example.odyssea.services.booking;

import com.example.odyssea.daos.booking.BookingDao;
import com.example.odyssea.daos.booking.BookingOptionDao;
import com.example.odyssea.entities.booking.Booking;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingOptionService {

    private final BookingOptionDao bookingOptionDao;
    private final CurrentUserService currentUserService;
    private final BookingDao bookingDao;      // injecte aussi BookingDao

    public BookingOptionService(BookingOptionDao bookingOptionDao,
                                CurrentUserService currentUserService,
                                BookingDao bookingDao) {
        this.bookingOptionDao = bookingOptionDao;
        this.currentUserService = currentUserService;
        this.bookingDao = bookingDao;
    }

    @Transactional
    public void addOptionToBooking(int itineraryId, int optionId) {
        int userId = currentUserService.getCurrentUserId();

        // 1) trouve la réservation existante pour cet utilisateur et cet itinéraire
        Booking booking = bookingDao.findById(userId, itineraryId);
        int bookingId = booking.getId();

        // 2) insert dans booking_option via la PK booking_id
        bookingOptionDao.insertBookingOption(bookingId, optionId);
    }
}

