package com.example.odyssea.daos.booking;

import com.example.odyssea.entities.mainTables.Option;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingOptionDao {
    private final JdbcTemplate jdbcTemplate;

    public BookingOptionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Option> getBookingOptions(int bookingId) {
        String sql = "SELECT o.* " +
                "FROM options o " +
                "INNER JOIN `booking` ro ON o.id = ro.option_id " +
                "INNER JOIN booking r ON r.booking = ro.booking " +
                "WHERE r.booking = ?";

        return jdbcTemplate.query(sql, new Object[]{bookingId}, new BeanPropertyRowMapper<>(Option.class));
    }

    // Ajouter une option d'une réservation
    public void insertBooking(int userId, int itineraryId, int optionId) {
        String sql = "INSERT INTO booking (user_id, itinerary_id, option_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, itineraryId, optionId);
    }

    //Supprimer une option d'une réservation
    public void deleteOptionFromBooking(int userId, int itineraryId, int optionId) {
        String sql = "DELETE FROM booking WHERE user_id = ? AND itinerary_id = ? AND option_id = ?";
        jdbcTemplate.update(sql, userId, itineraryId, optionId);
    }
}
