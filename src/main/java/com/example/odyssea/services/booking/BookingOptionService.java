// src/main/java/com/example/odyssea/services/booking/BookingOptionService.java
package com.example.odyssea.services.booking;

import com.example.odyssea.daos.booking.BookingOptionDao;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingOptionService {

    private final BookingOptionDao bookingOptionDao;
    private final CurrentUserService currentUserService;

    public BookingOptionService(BookingOptionDao bookingOptionDao,
                                CurrentUserService currentUserService) {
        this.bookingOptionDao = bookingOptionDao;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public void addOptionToBooking(int itineraryId, int optionId) {
        int userId = currentUserService.getCurrentUserId();
        bookingOptionDao.insertBooking(userId, itineraryId, optionId);
    }
}
