package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.userItinerary.UserItinerary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationOptionDao {
    private final JdbcTemplate jdbcTemplate;

    public ReservationOptionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Option> getBookingOptions(int bookingId) {
        String sql = "SELECT o.* " +
                "FROM options o " +
                "INNER JOIN `reservation_option` ro ON o.id = ro.option_id " +
                "INNER JOIN reservation r ON r.reservation_id = ro.reservation_id " +
                "WHERE r.reservation_id = ?";

        return jdbcTemplate.query(sql, new Object[]{bookingId}, new BeanPropertyRowMapper<>(Option.class));
    }

    // Ajouter une option d'une réservation
    public void insertReservationOption(int userId, int itineraryId, int optionId) {
        String sql = "INSERT INTO reservation_option (user_id, itinerary_id, option_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, itineraryId, optionId);
    }

    //Supprimer une option d'une réservation
    public void deleteOptionFromReservation(int userId, int itineraryId, int optionId) {
        String sql = "DELETE FROM reservation_option WHERE user_id = ? AND itinerary_id = ? AND option_id = ?";
        jdbcTemplate.update(sql, userId, itineraryId, optionId);
    }
}
