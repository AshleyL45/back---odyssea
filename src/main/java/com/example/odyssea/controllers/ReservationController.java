package com.example.odyssea.controllers;

import com.example.odyssea.dtos.reservation.ItineraryReservationDTO;
import com.example.odyssea.dtos.reservation.ReservationRecapDTO;
import com.example.odyssea.dtos.reservation.ReservationRequestDTO;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.entities.mainTables.Reservation;
import com.example.odyssea.services.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ItineraryReservationDTO>> getAllUserReservations(@PathVariable int userId){
        return ResponseEntity.ok(reservationService.getAllUserReservations(userId));
    }

    @GetMapping("/{userId}/{itineraryId}")
    public ResponseEntity<Reservation> getReservation(@PathVariable int userId, @PathVariable int itineraryId) {
        return ResponseEntity.ok(reservationService.getReservation(userId, itineraryId));
    }

    @GetMapping("/details/{userId}/{itineraryId}")
    public ResponseEntity<ReservationRecapDTO> getReservationDetails(@PathVariable int userId, @PathVariable int itineraryId){
        return ResponseEntity.ok(reservationService.getReservationDetails(userId, itineraryId));
    }

    @GetMapping("/lastDone/{userId}/{status}")
    public ResponseEntity<Itinerary> getLastDoneReservation(@PathVariable int userId, @PathVariable String status){
        return ResponseEntity.ok(reservationService.getLastDoneReservation(userId, status));
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody ReservationRequestDTO reservationRequest) {
        Reservation createdReservation = reservationService.createReservation(reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @PatchMapping("/{userId}/{itineraryId}")
    public ResponseEntity<String> updateReservationStatus(@PathVariable int userId, @PathVariable int itineraryId, @Valid @RequestBody String status){
       boolean isUpdated = reservationService.updateReservationStatus(userId, itineraryId, status);
       if(isUpdated){
           return ResponseEntity.ok("Reservation successfully updated.");
       } else {
           return ResponseEntity.badRequest().body("Cannot update reservation of user id : " + userId + " with itinerary id : " + itineraryId);
       }
    }

    @PutMapping("/{userId}/{itineraryId}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable int userId, @PathVariable int itineraryId, @Valid @RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.updateReservation(userId, itineraryId, reservation));
    }

    @DeleteMapping("/{userId}/{itineraryId}")
    public ResponseEntity<String> deleteReservation(@PathVariable int userId, @PathVariable int itineraryId) {
        boolean isDeleted = reservationService.deleteReservation(userId, itineraryId);
        if(isDeleted){
            return ResponseEntity.ok("Reservation successfully deleted.");
        } else {
            return ResponseEntity.badRequest().body("Cannot delete reservation of user id : " + userId + "with itinerary id : " + itineraryId);
        }
    }
}
