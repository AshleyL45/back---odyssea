package com.example.odyssea.services.booking.draft;

import com.example.odyssea.daos.booking.BookingDraftDao;
import com.example.odyssea.daos.booking.BookingOptionDraftDao;
import com.example.odyssea.daos.booking.BookingOptionDao;
import com.example.odyssea.daos.booking.BookingDao;
import com.example.odyssea.entities.booking.Booking;
import com.example.odyssea.entities.booking.BookingDraft;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.enums.BookingStatus;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingDraftFinalizerService {
    private final BookingDraftDao bookingDraftDao;
    private final BookingOptionDraftDao bookingOptionDraftDao;
    private final BookingDao bookingDao;
    private final BookingOptionDao bookingOptionDao;
    private final CurrentUserService currentUserService;

    public BookingDraftFinalizerService(BookingDraftDao bookingDraftDao,
                                        BookingOptionDraftDao bookingOptionDraftDao,
                                        BookingDao bookingDao,
                                        BookingOptionDao bookingOptionDao,
                                        CurrentUserService currentUserService) {
        this.bookingDraftDao = bookingDraftDao;
        this.bookingOptionDraftDao = bookingOptionDraftDao;
        this.bookingDao = bookingDao;
        this.bookingOptionDao = bookingOptionDao;
        this.currentUserService = currentUserService;
    }

    private Integer getUserId() {
        return currentUserService.getCurrentUserId();
    }

    /**
     * Étape 5 : transforme le draft en réservation définitive et nettoie les tables draft
     */
    @Transactional
    public void finalizeBookingDraft() {
        Integer userId = getUserId();
        BookingDraft draft = bookingDraftDao.getLastDraftByUserId(userId);
        Integer draftId = draft.getDraftId();

        // calcul retour si absent
        LocalDate departure = draft.getDepartureDate();
        LocalDate returnDate = draft.getReturnDate() != null
                ? draft.getReturnDate()
                : departure.plusDays(12);

        // construction de la réservation
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

        // sauvegarde
        bookingDao.save(booking);

        // options du draft
        List<Option> options = bookingOptionDraftDao.getOptionsByDraftId(draftId);
        bookingOptionDao.deleteOptionsForBooking(
                userId,
                draft.getItineraryId()
        );
        for (Option opt : options) {
            bookingOptionDao.insertBookingOption(
                    userId,
                    draft.getItineraryId(),
                    opt.getId()
            );
        }

        // nettoyage des drafts
        bookingOptionDraftDao.deleteOptionsByDraftId(draftId);
        bookingDraftDao.deleteDraftByDraftId(draftId);
    }
}
