package com.example.odyssea.services.booking;

import com.example.odyssea.daos.booking.BookingDraftDao;
import com.example.odyssea.daos.mainTables.OptionDao;
import com.example.odyssea.daos.booking.BookingOptionDraftDao;
import com.example.odyssea.entities.booking.BookingDraft;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BookingDraftService {
    private final BookingDraftDao bookingDraftDao;
    private final BookingOptionDraftDao bookingOptionDraftDao;
    private final CurrentUserService currentUserService;
    private final OptionDao optionDao;

    public BookingDraftService(BookingDraftDao bookingDraftDao, BookingOptionDraftDao bookingOptionDraftDao, CurrentUserService currentUserService, OptionDao optionDao) {
        this.bookingDraftDao = bookingDraftDao;
        this.bookingOptionDraftDao = bookingOptionDraftDao;
        this.currentUserService = currentUserService;
        this.optionDao = optionDao;
    }

    private Integer getUserId() {
        return currentUserService.getCurrentUserId();
    }

    public void validateNumberOfAdults(Integer numberAdults) {
        if (numberAdults == null || numberAdults <= 0) {
            throw new ValidationException("There must be at least one adult");
        }
        Integer draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
        bookingDraftDao.updateNumberOfAdults(draftId, numberAdults);
    }

    public void validateNumberOfKids(Integer numberKids) {
        if (numberKids == null || numberKids < 0) {
            throw new ValidationException("Number of kids cannot be negative");
        }
        Integer draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
        bookingDraftDao.updateNumberOfKids(draftId, numberKids);
    }

    public void validateDepartureDate(String date) {
        if (date == null || date.isBlank()) {
            throw new ValidationException("Departure date is required");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate = LocalDate.parse(date, formatter);

        if (parsedDate.isBefore(LocalDate.now().plusDays(1))) {
            throw new ValidationException("Departure date must be at least tomorrow");
        }
        Integer draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
        bookingDraftDao.updateDepartureDate(draftId, parsedDate);
    }

    public void validateItineraryId(Integer itineraryId) {
        if (itineraryId == null || itineraryId <= 0) {
            throw new ValidationException("Itinerary ID must be a positive number");
        }
        Integer draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
        bookingDraftDao.updateItineraryId(draftId, itineraryId);
    }

    public void validateType(String type) {
        if (type == null || (!type.equals("Standard") && !type.equals("Mystery"))) {
            throw new ValidationException("Type must be either Standard or Mystery");
        }
        Integer draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
        bookingDraftDao.updateType(draftId, type);
    }

    @Transactional
    public void validateOptions(List<Integer> optionIds) {
        if (optionIds == null || optionIds.isEmpty()) {
            return;
        }

        Integer draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
        bookingOptionDraftDao.deleteOptionsByDraftId(draftId);

        for (Integer optionId : optionIds) {
            optionDao.findById(optionId);
        }
    }

    @Transactional(readOnly = true)
    public BookingDraft loadDraft() {
        return bookingDraftDao.getLastDraftByUserId(getUserId());
    }
}
