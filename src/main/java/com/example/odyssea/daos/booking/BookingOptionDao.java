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

    public void insertBooking(int userId, int itineraryId, int optionId) {
        String sql = "INSERT INTO booking_option (user_id, itinerary_id, option_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, itineraryId, optionId);
    }


    //Supprimer une option d'une réservation
    /** Supprime toutes les options pour un couple (user, itinerary) donné */
    public void deleteOptionsForBooking(int userId, int itineraryId) {
        String sql = "DELETE FROM booking_option WHERE user_id = ? AND itinerary_id = ?";
        jdbcTemplate.update(sql, userId, itineraryId);
    }

    /** Insère une option pour un couple (user, itinerary) donné */
    public void insertBookingOption(int userId, int itineraryId, int optionId) {
        String sql = "INSERT INTO booking_option (user_id, itinerary_id, option_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, itineraryId, optionId);
    }


}
