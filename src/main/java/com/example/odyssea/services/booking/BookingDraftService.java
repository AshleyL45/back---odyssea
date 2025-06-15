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


        // 1) Calcul de la date de retour si non stockée
        LocalDate departure = draft.getDepartureDate();
        LocalDate returnDate = (draft.getReturnDate() != null)
                ? draft.getReturnDate()
                : departure.plusDays(12);

        // 2) Construction de l'entité Booking
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setItineraryId(draft.getItineraryId());
        booking.setDepartureDate(departure);
        booking.setReturnDate(returnDate);
        booking.setNumberOfAdults(draft.getNumberOfAdults());
        booking.setNumberOfKids(draft.getNumberOfKids());
        booking.setStatus(
                draft.getType().equals("Mystery")
                        ? BookingStatus.PENDING.name()
                        : BookingStatus.CONFIRMED.name()
        );
        booking.setPurchaseDate(LocalDate.now());
        booking.setTotalPrice(BigDecimal.ZERO);
        booking.setType(draft.getType());

        // 3) Persistance de la réservation dans la table booking
        bookingDao.save(booking);

        // 4) Récupération des options sélectionnées dans le draft
        List<Option> options = bookingOptionDraftDao.getOptionsByDraftId(draft.getDraftId());

        // 5) Suppression des anciennes options pour ce user+itinerary
        bookingOptionDao.deleteOptionsForBooking(userId, draft.getItineraryId());

        // 6) Insertion des options actuelles
        for (Option opt : options) {
            bookingOptionDao.insertBookingOption(userId, draft.getItineraryId(), opt.getId());
        }

        bookingOptionDraftDao.deleteOptionsByDraftId(draft.getDraftId());

        // 2️⃣ Puis seulement on supprime le draft
        bookingDraftDao.deleteDraftByDraftId(draft.getDraftId());

    }

    @Service
    public static class BookingStep1Service {
        private final BookingDraftDao bookingDraftDao;
        private final CurrentUserService currentUserService;

        public BookingStep1Service(BookingDraftDao bookingDraftDao,
                                   CurrentUserService currentUserService) {
            this.bookingDraftDao = bookingDraftDao;
            this.currentUserService = currentUserService;
        }

        private int getUserId() {
            return currentUserService.getCurrentUserId();
        }

        /**
         * Étape 1 : validation itinéraire, type et date de départ
         */
        public void execute(int itineraryId, String type, String date) {
            if (itineraryId <= 0) {
                throw new ValidationException("Itinerary ID must be a positive number");
            }
            if (!"Standard".equals(type) && !"Mystery".equals(type)) {
                throw new ValidationException("Type must be either Standard or Mystery");
            }
            if (date == null || date.isBlank()) {
                throw new ValidationException("Departure date is required");
            }
            LocalDate departure = LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (departure.isBefore(LocalDate.now().plusDays(1))) {
                throw new ValidationException("Departure date must be at least tomorrow");
            }

            int draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
            bookingDraftDao.updateItineraryId(draftId, itineraryId);
            bookingDraftDao.updateType(draftId, type);
            bookingDraftDao.updateDepartureDate(draftId, departure);
        }
    }

    @Service
    public static class BookingStep2Service {
        private final BookingDraftDao bookingDraftDao;
        private final CurrentUserService currentUserService;

        public BookingStep2Service(BookingDraftDao bookingDraftDao,
                                   CurrentUserService currentUserService) {
            this.bookingDraftDao = bookingDraftDao;
            this.currentUserService = currentUserService;
        }

        private int getUserId() {
            return currentUserService.getCurrentUserId();
        }

        /**
         * Étape 2 : validation nombre d’adultes et d’enfants
         */
        public void execute(int numberAdults, int numberKids) {
            if (numberAdults <= 0) {
                throw new ValidationException("There must be at least one adult");
            }
            if (numberKids < 0) {
                throw new ValidationException("Number of kids cannot be negative");
            }
            int draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
            bookingDraftDao.updateNumberOfAdults(draftId, numberAdults);
            bookingDraftDao.updateNumberOfKids(draftId, numberKids);
        }
    }

    @Service
    public static class BookingStep3Service {
        private final BookingDraftDao bookingDraftDao;
        private final BookingOptionDraftDao bookingOptionDraftDao;
        private final OptionDao optionDao;
        private final CurrentUserService currentUserService;

        public BookingStep3Service(BookingDraftDao bookingDraftDao,
                                   BookingOptionDraftDao bookingOptionDraftDao,
                                   OptionDao optionDao,
                                   CurrentUserService currentUserService) {
            this.bookingDraftDao = bookingDraftDao;
            this.bookingOptionDraftDao = bookingOptionDraftDao;
            this.optionDao = optionDao;
            this.currentUserService = currentUserService;
        }

        private int getUserId() {
            return currentUserService.getCurrentUserId();
        }

        /**
         * Étape 3 : enregistre la liste d’options choisies
         */
        @Transactional
        public void execute(List<Integer> optionIds) {
            int draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
            // purge anciennes
            bookingOptionDraftDao.deleteOptionsByDraftId(draftId);
            if (optionIds == null || optionIds.isEmpty()) {
                return;
            }
            // validation existence
            for (Integer optId : optionIds) {
                if (optionDao.findById(optId) == null) {
                    throw new ValidationException("Option not found: " + optId);
                }
            }
            bookingOptionDraftDao.saveOptions(draftId, optionIds);
        }
    }
}


