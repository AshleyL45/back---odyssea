package com.example.odyssea.services;

import com.example.odyssea.daos.ReservationDao;
import com.example.odyssea.dtos.ReservationDto;
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

    public Reservation getReservation(int id) {
        return reservationDao.findById(id);
    }

    public Reservation createReservation(ReservationDto reservationDto) {
        Reservation reservation = reservationDto.toEntity();
        return reservationDao.save(reservation);
    }

    public Reservation updateReservation(int id, ReservationDto reservationDto) {
        Reservation reservation = reservationDto.toEntity();
        return reservationDao.update(id, reservation);
    }

    public boolean deleteReservation(int id) {
        return reservationDao.delete(id);
    }
}
