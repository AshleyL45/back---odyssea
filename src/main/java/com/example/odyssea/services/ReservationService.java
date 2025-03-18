package com.example.odyssea.services;

import com.example.odyssea.daos.ItineraryDao;
import com.example.odyssea.daos.OptionDao;
import com.example.odyssea.daos.ReservationDao;
import com.example.odyssea.dtos.reservation.ItineraryReservationDTO;
import com.example.odyssea.dtos.reservation.ReservationRecapDTO;
import com.example.odyssea.dtos.reservation.ReservationRequestDTO;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.entities.mainTables.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ItineraryDao itineraryDao;
    private final OptionDao optionDao;

    public ReservationService(ReservationDao reservationDao, ItineraryDao itineraryDao, OptionDao optionDao) {
        this.reservationDao = reservationDao;
        this.itineraryDao = itineraryDao;
        this.optionDao = optionDao;
    }

    private BigDecimal calculateTotalPrice(ReservationRequestDTO reservationRequest) {
        BigDecimal itineraryPrice = itineraryDao.findById(reservationRequest.getItineraryId()).getPrice();
        BigDecimal optionPrice = BigDecimal.ZERO;

        for (Integer optionId : reservationRequest.getOptionIds()) {
            BigDecimal optionItemPrice = optionDao.findById(optionId).getPrice();
            optionPrice = optionPrice.add(optionItemPrice);
        }

        BigDecimal totalPrice = itineraryPrice
                .multiply(BigDecimal.valueOf(reservationRequest.getNumberOfAdults()))
                .add(itineraryPrice.multiply(BigDecimal.valueOf(reservationRequest.getNumberOfKids())))
                .add(optionPrice);
        return totalPrice;
    }

    public Reservation createReservation(ReservationRequestDTO reservationRequest) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate departureDate = LocalDate.parse(reservationRequest.getDepartureDate(), formatter);
        LocalDate returnDate = LocalDate.parse(reservationRequest.getReturnDate(), formatter);


        BigDecimal totalPrice = calculateTotalPrice(reservationRequest);

        Reservation reservation = new Reservation(
                reservationRequest.getUserId(),
                reservationRequest.getItineraryId(),
                reservationRequest.getStatus(),
                departureDate,
                returnDate,
                totalPrice,
                LocalDate.now(),
                reservationRequest.getNumberOfAdults(),
                reservationRequest.getNumberOfKids(),
                new ArrayList<>()
        );

        reservationDao.save(reservation);

        if (reservationRequest.getOptionIds() != null && !reservationRequest.getOptionIds().isEmpty()) {
            for (Integer optionId : reservationRequest.getOptionIds()) {
                reservationDao.insertOptions(reservation);
            }
        }

        return reservationDao.getReservationWithOptions(reservationRequest.getUserId(), reservationRequest.getItineraryId());
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.findAll();
    }

    public Reservation getReservation(int userId, int itineraryId) {
        return reservationDao.findById(userId, itineraryId);
    }

    public List<ItineraryReservationDTO> getAllUserReservations(int userId){
        return reservationDao.findAllUserReservations(userId);
    }

    public ReservationRecapDTO getReservationDetails(int userId, int itineraryId){
        return reservationDao.findReservationDetails(userId, itineraryId);
    }

    public Itinerary getLastDoneReservation(int userId, String status){
        return reservationDao.findLastDoneItinerary(userId, status);
    }


    public boolean updateReservationStatus(int userId, int itineraryId, String status){
        return reservationDao.updateReservationStatus(userId, itineraryId, status);
    }

    public Reservation updateReservation(int userId, int itineraryId, Reservation reservation) {
        return reservationDao.update(userId, itineraryId, reservation);
    }

    public boolean deleteReservation(int userId, int itineraryId) {
        return reservationDao.delete(userId, itineraryId);
    }
}
