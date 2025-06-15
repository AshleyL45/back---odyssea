package com.example.odyssea.services.booking.draft;

import com.example.odyssea.daos.booking.BookingDraftDao;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class BookingDraftStep1Service {
    private final BookingDraftDao bookingDraftDao;
    private final CurrentUserService currentUserService;

    public BookingDraftStep1Service(BookingDraftDao bookingDraftDao,
                                    CurrentUserService currentUserService) {
        this.bookingDraftDao = bookingDraftDao;
        this.currentUserService = currentUserService;
    }

    private Integer getUserId() {
        return currentUserService.getCurrentUserId();
    }

    /**
     * Étape 1 : validation itinéraire, type et date de départ
     */
    public void execute(Integer itineraryId, String type, String date) {
        if (itineraryId == null || itineraryId <= 0) {
            throw new ValidationException("Itinerary ID must be a positive number");
        }
        if (!"Standard".equals(type) && !"Mystery".equals(type)) {
            throw new ValidationException("Type must be either Standard or Mystery");
        }
        if (date == null || date.isBlank()) {
            throw new ValidationException("Departure date is required");
        }
        LocalDate departure = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (departure.isBefore(LocalDate.now().plusDays(1))) {
            throw new ValidationException("Departure date must be at least tomorrow");
        }

        Integer draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
        bookingDraftDao.updateItineraryId(draftId, itineraryId);
        bookingDraftDao.updateType(draftId, type);
        bookingDraftDao.updateDepartureDate(draftId, departure);
    }

    public void updateItineraryOnly(int newItineraryId) {
        if (newItineraryId <= 0) {
            throw new ValidationException("Itinerary ID must be positive");
        }
        int draftId = bookingDraftDao.getLastDraftIdByUser(currentUserService.getCurrentUserId());
        bookingDraftDao.updateItineraryId(draftId, newItineraryId);
    }
}
