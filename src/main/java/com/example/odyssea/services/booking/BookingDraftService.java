package com.example.odyssea.services.booking;

import com.example.odyssea.daos.booking.BookingDao;
import com.example.odyssea.daos.booking.BookingDraftDao;
import com.example.odyssea.daos.booking.BookingOptionDao;
import com.example.odyssea.daos.mainTables.OptionDao;
import com.example.odyssea.daos.booking.BookingOptionDraftDao;
import com.example.odyssea.entities.booking.Booking;
import com.example.odyssea.entities.booking.BookingDraft;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.enums.BookingStatus;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingDraftService {
    private final BookingDraftDao bookingDraftDao;
    private final BookingOptionDraftDao bookingOptionDraftDao;
    private final CurrentUserService currentUserService;
    private final OptionDao optionDao;
    private final BookingDao bookingDao;
    private final BookingOptionDao bookingOptionDao;

    public BookingDraftService(
            BookingDraftDao bookingDraftDao,
            BookingOptionDraftDao bookingOptionDraftDao,
            CurrentUserService currentUserService,
            OptionDao optionDao,
            BookingDao bookingDao,
            BookingOptionDao bookingOptionDao) {
        this.bookingDraftDao = bookingDraftDao;
        this.bookingOptionDraftDao = bookingOptionDraftDao;
        this.currentUserService = currentUserService;
        this.optionDao = optionDao;
        this.bookingDao = bookingDao;
        this.bookingOptionDao = bookingOptionDao;
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
        Integer draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());

        // 1) on supprime d’abord l’ancien jeu d’options
        bookingOptionDraftDao.deleteOptionsByDraftId(draftId);

        // 2) si la liste est vide, on s’arrête là
        if (optionIds == null || optionIds.isEmpty()) {
            return;
        }

        // 3) on vérifie que chaque option existe
        for (Integer optionId : optionIds) {
            optionDao.findById(optionId);
        }

        // 4) **on enregistre enfin les options du draft**
        bookingOptionDraftDao.saveOptions(draftId, optionIds);
    }

    @Transactional(readOnly = true)
    public BookingDraft loadDraft() {
        return bookingDraftDao.getLastDraftByUserId(getUserId());
    }

    public void overrideItinerary(Integer newItineraryId) {
        if (newItineraryId == null || newItineraryId <= 0) {
            throw new ValidationException("Itinerary ID must be positive");
        }
        Integer draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
        bookingDraftDao.updateItineraryId(draftId, newItineraryId);
    }

    @Transactional
    public void finalizeBookingDraft() {
        Integer userId = getUserId();
        BookingDraft draft = bookingDraftDao.getLastDraftByUserId(userId);

        // Calcul de la date de retour si non stockée
        LocalDate departure = draft.getDepartureDate();
        LocalDate returnDate = (draft.getReturnDate() != null)
                ? draft.getReturnDate()
                : departure.plusDays(12);

        // Construction de l'entité finale
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setItineraryId(draft.getItineraryId());
        booking.setDepartureDate(departure);
        booking.setReturnDate(returnDate);
        booking.setNumberOfAdults(draft.getNumberOfAdults());
        booking.setNumberOfKids(draft.getNumberOfKids());
        booking.setStatus(draft.getType().equals("Mystery")
                ? BookingStatus.PENDING.name()
                : BookingStatus.CONFIRMED.name());
        booking.setPurchaseDate(LocalDate.now());
        booking.setTotalPrice(BigDecimal.ZERO);
        booking.setType(draft.getType());

        // Enregistrement dans booking
        Booking saved = bookingDao.save(booking);

        // Transfert des options du draft
        List<Option> options = bookingOptionDraftDao.getOptionsByDraftId(draft.getDraftId());
        List<Integer> optionIds = options.stream()
                .map(Option::getId)
                .collect(Collectors.toList());
        for (Integer optionId : optionIds) {
            // insertBooking ajoute une entrée dans booking_option
            bookingOptionDao.insertBooking(userId, draft.getItineraryId(), optionId);
        }
    }
}


