package com.example.odyssea.daos.mainTables;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationOptionDao {
    private final JdbcTemplate jdbcTemplate;

    public ReservationOptionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Ajouter une option d'une réservation
    public void insertReservationOption(int userId, int itineraryId, int optionId) {
        String sql = "INSERT INTO reservationOption (user_id, itinerary_id, option_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, itineraryId, optionId);
    }

    //Supprimer une option d'une réservation
    public void deleteOptionFromReservation(int userId, int itineraryId, int optionId) {
        String sql = "DELETE FROM reservationOption WHERE user_id = ? AND itinerary_id = ? AND option_id = ?";
        jdbcTemplate.update(sql, userId, itineraryId, optionId);
    }
}
