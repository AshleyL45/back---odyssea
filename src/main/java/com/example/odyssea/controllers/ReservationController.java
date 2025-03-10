package com.example.odyssea.controllers;

import com.example.odyssea.dtos.ReservationDto;
import com.example.odyssea.entities.mainTables.Reservation;
import com.example.odyssea.services.ReservationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Reservation getReservation(@PathVariable int id) {
        return reservationService.getReservation(id);
    }

    @PostMapping
    public Reservation createReservation(@RequestBody ReservationDto reservationDto) {
        return reservationService.createReservation(reservationDto);
    }

    @PutMapping("/{id}")
    public Reservation updateReservation(@PathVariable int id, @RequestBody ReservationDto reservationDto) {
        return reservationService.updateReservation(id, reservationDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteReservation(@PathVariable int id) {
        return reservationService.deleteReservation(id);
    }
}
