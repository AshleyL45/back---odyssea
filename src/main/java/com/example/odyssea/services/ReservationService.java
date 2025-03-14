package com.example.odyssea.services;

import com.example.odyssea.daos.ReservationDao;
import com.example.odyssea.dtos.ItineraryReservationDTO;
import com.example.odyssea.dtos.ReservationRecapDTO;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.entities.mainTables.Reservation;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
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

    public Reservation createReservation(Reservation reservation) {
        return reservationDao.save(reservation);
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
