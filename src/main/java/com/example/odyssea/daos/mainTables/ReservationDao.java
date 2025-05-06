package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.Reservation;
import com.example.odyssea.exceptions.DatabaseException;
import com.example.odyssea.exceptions.ReservationNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> reservationRowMapper = (rs, _) -> new Reservation(
            rs.getInt("reservationId"),
            rs.getInt("userId"),
            rs.getInt("itineraryId"),
            rs.getString("status"),
            rs.getDate("departureDate").toLocalDate(),
            rs.getDate("returnDate").toLocalDate(),
            rs.getBigDecimal("totalPrice"),
            rs.getDate("purchaseDate").toLocalDate(),
            rs.getInt("numberOfAdults"),
            rs.getInt("numberOfKids"),
            rs.getString("type")
    );


    // Récupère la liste de toutes les réservations
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservation";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    // Récupère une réservation
    public Reservation findById(int userId, int bookingId) {
        String sql = "SELECT * FROM reservation WHERE userId = ? AND reservationId = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, userId, bookingId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ReservationNotFoundException("Booking " + bookingId + "does not exist"));
    }

    //Récupère tous les itinéraires reservés d'un utilisateur
    public List<Reservation> findAllUserReservations(int userId) {
        String sql = "SELECT * FROM reservation WHERE reservation.userId = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, userId);
    }


    // Enregistre une nouvelle réservation dans la base
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (userId, itineraryId, status, departureDate, returnDate, totalPrice, purchaseDate, numberOfAdults, numberOfKids, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                reservation.getUserId(),
                reservation.getItineraryId(),
                reservation.getStatus(),
                reservation.getDepartureDate(),
                reservation.getReturnDate(),
                reservation.getTotalPrice(),
                reservation.getPurchaseDate(),
                reservation.getNumberOfAdults(),
                reservation.getNumberOfKids(),
                reservation.getType()
        );

        return reservation;
    }

    // Met à jour une réservation existante identifiée par son ID
    public Reservation update(int userId, int bookingId, Reservation reservation) {
        if (!reservationExists(bookingId)) {
            throw new ReservationNotFoundException("Booking " + bookingId + "does not exist");
        }

        String sql = "UPDATE reservation SET userId = ?, itineraryId = ?, status = ?, departureDate = ?, returnDate = ?, totalPrice = ?, purchaseDate = ?, numberOfAdults = ?, numberOfKids = ?, type = ? WHERE userId = ? AND bookingId = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                reservation.getUserId(),
                reservation.getItineraryId(),
                reservation.getStatus(),
                reservation.getDepartureDate(),
                reservation.getReturnDate(),
                reservation.getTotalPrice(),
                reservation.getPurchaseDate(),
                reservation.getNumberOfAdults(),
                reservation.getNumberOfKids(),
                bookingId,
                userId
        );

        if (rowsAffected <= 0) {
            throw new DatabaseException("Failed to update reservation with booking ID :  " + bookingId);
        }

        return this.findById(userId, bookingId);
    }

    // Changer le status d'une réservation
    public void updateReservationStatus(int userId, int bookingId, String status){
        if(!reservationExists(bookingId)) {
            throw new ReservationNotFoundException("Booking of user " + userId + " with booking ID " + bookingId + "does not exist");
        }

        String sql = "UPDATE reservation SET status = ? WHERE userId = ? AND reservationId = ?";

        int rowsAffected = jdbcTemplate.update(sql, status, userId, bookingId);

        if(rowsAffected <= 0){
            throw new DatabaseException("Failed to update bookingId with user ID :  " + userId + "with booking ID : " + bookingId);
        }
    }

    // Vérifie si une réservation existe dans la base
    private boolean reservationExists(int bookingId) {
        String checkSql = "SELECT COUNT(*) FROM reservation WHERE reservationId = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, bookingId);
        return count > 0;
    }

    // Supprime une réservation par son identifiant
    public void delete(int bookingId) {
        String sql = "DELETE FROM reservation WHERE reservationId = ?";
        int rowsAffected = jdbcTemplate.update(sql, bookingId);
        if(rowsAffected <= 0){
            throw new DatabaseException("Failed to delete booking.");
        }
    }
}
