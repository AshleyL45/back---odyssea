package com.example.odyssea.daos;

import com.example.odyssea.entities.mainTables.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationDao {

    // JdbcTemplate permet d'accéder à la base de données
    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> new Reservation(
            rs.getInt("id"),
            rs.getInt("userId"),
            rs.getInt("itineraryId"),
            rs.getString("status"),
            rs.getDate("departureDate"),
            rs.getDate("returnDate"),
            rs.getDouble("totalPrice"),
            rs.getDate("purchaseDate"),
            rs.getInt("numberOfAdults"),
            rs.getInt("numberOfKids")
    );

    // Récupère la liste de toutes les réservations
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservation";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    // Récupère une réservation par son identifiant
    public Reservation findById(int id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Reservation with ID: " + id + " does not exist"));
    }

    // Enregistre une nouvelle réservation dans la base
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (userId, itineraryId, status, departureDate, returnDate, totalPrice, purchaseDate, numberOfAdults, numberOfKids) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                reservation.getIdUser(),
                reservation.getIdItinerary(),
                reservation.getStatus(),
                reservation.getDepartureDate(),
                reservation.getReturnDate(),
                reservation.getTotalPrice(),
                reservation.getPurchase(),
                reservation.getNumberOfAdults(),
                reservation.getNumberOfKids()
        );

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);
        reservation.setIdReservation(id);
        return reservation;
    }

    // Met à jour une réservation existante identifiée par son ID
    public Reservation update(int id, Reservation reservation) {
        if (!reservationExists(id)) {
            throw new RuntimeException("Reservation with ID: " + id + " does not exist");
        }

        String sql = "UPDATE reservation SET userId = ?, itineraryId = ?, status = ?, departureDate = ?, returnDate = ?, totalPrice = ?, purchaseDate = ?, numberOfAdults = ?, numberOfKids = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                reservation.getIdUser(),
                reservation.getIdItinerary(),
                reservation.getStatus(),
                reservation.getDepartureDate(),
                reservation.getReturnDate(),
                reservation.getTotalPrice(),
                reservation.getPurchase(),
                reservation.getNumberOfAdults(),
                reservation.getNumberOfKids(),
                id
        );

        if (rowsAffected <= 0) {
            throw new RuntimeException("Failed to update reservation with ID: " + id);
        }

        return this.findById(id);
    }

    // Vérifie si une réservation existe dans la base
    private boolean reservationExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM product WHERE id_product = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }

    // Supprime une réservation par son identifiant
    public boolean delete(int id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
}
