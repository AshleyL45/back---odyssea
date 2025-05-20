package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.ReservationDraftDao;
import com.example.odyssea.daos.itinerary.ItineraryDao;
import com.example.odyssea.daos.mainTables.OptionDao;
import com.example.odyssea.daos.mainTables.ReservationDao;
import com.example.odyssea.daos.mainTables.ReservationOptionDao;
import com.example.odyssea.daos.mainTables.ReservationOptionDraftDao;
import com.example.odyssea.dtos.reservation.BookingConfirmation;
import com.example.odyssea.entities.ReservationDraft;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.mainTables.Reservation;
import com.example.odyssea.exceptions.ReservationNotFoundException;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationOptionDao reservationOptionDao;
    private final ItineraryDao itineraryDao;
    private final OptionDao optionDao;
    private final CurrentUserService currentUserService;
    private final ReservationDraftDao reservationDraftDao;
    private final ReservationOptionDraftDao reservationOptionDraftDao;


    public ReservationService(ReservationDao reservationDao, ReservationOptionDao reservationOptionDao, ItineraryDao itineraryDao, OptionDao optionDao, CurrentUserService currentUserService, ReservationDraftDao reservationDraftDao, ReservationOptionDraftDao reservationOptionDraftDao) {
        this.reservationDao = reservationDao;
        this.reservationOptionDao = reservationOptionDao;
        this.itineraryDao = itineraryDao;
        this.optionDao = optionDao;
        this.currentUserService = currentUserService;
        this.reservationDraftDao = reservationDraftDao;
        this.reservationOptionDraftDao = reservationOptionDraftDao;
    }


    @Transactional
    public void createReservation() {
        Integer userId = getUserId();
        ReservationDraft draft = reservationDraftDao.getLastDraftByUserId(userId);

        if (draft.getUserId() == null || draft.getItineraryId() == null || draft.getDepartureDate() == null) {
            throw new ValidationException("Missing required draft data");
        }

        LocalDate returnDate = draft.getDepartureDate().plusDays(12);
        BigDecimal totalPrice = calculateTotalPrice(draft);

        Reservation reservation = new Reservation();
        reservation.setUserId(draft.getUserId());
        reservation.setItineraryId(draft.getItineraryId());
        reservation.setDepartureDate(draft.getDepartureDate());
        reservation.setReturnDate(returnDate);
        reservation.setNumberOfAdults(draft.getNumberOfAdults());
        reservation.setNumberOfKids(draft.getNumberOfKids());
        reservation.setType(draft.getType());
        reservation.setPurchaseDate(LocalDate.now());
        reservation.setTotalPrice(totalPrice);

        reservationDao.save(reservation);

        List<Integer> optionIds = reservationOptionDraftDao.getOptionsByDraftId(draft.getDraftId())
                .stream().map(Option::getId).toList();

        if(!optionIds.isEmpty()){
            reservationOptionDraftDao.saveOptions(draft.getDraftId(), optionIds);
        }
    }



    public List<BookingConfirmation> getAllUserReservations() {
        Integer userId = getUserId();
        return getBookingsAndMap(reservationDao.findAllUserReservations(userId));
    }


    public BookingConfirmation getBookingById(int bookingId) {
        Integer userId = getUserId();
        Reservation reservation = reservationDao.findById(userId, bookingId);

        if (reservation.getUserId() != userId) {
            throw new ReservationNotFoundException("Reservation does not belong to the current user.");
        }

        return createBookingConfirmation(reservation);
    }


    private Integer getUserId() {
        return currentUserService.getCurrentUserId();
    }

    private List<BookingConfirmation> getBookingsAndMap(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::createBookingConfirmation)
                .collect(Collectors.toList());
    }

    private BookingConfirmation createBookingConfirmation(Reservation reservation) {
        Itinerary itinerary = itineraryDao.findById(reservation.getItineraryId());
        List<Option> options = reservationOptionDao.getBookingOptions(reservation.getReservationId());
        return BookingConfirmation.fromEntities(reservation, itinerary, options);
    }


    private BigDecimal calculateTotalPrice(ReservationDraft draft) {
        BigDecimal itineraryPrice = itineraryDao.findById(draft.getItineraryId()).getPrice();
        BigDecimal optionPrice = reservationOptionDraftDao.getOptionsByDraftId(draft.getDraftId()).stream()
                .map(option -> optionDao.findById(option.getId()).getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return itineraryPrice
                .multiply(BigDecimal.valueOf(draft.getNumberOfAdults()))
                .add(itineraryPrice.multiply(BigDecimal.valueOf(draft.getNumberOfKids())))
                .add(optionPrice);
    }


}
