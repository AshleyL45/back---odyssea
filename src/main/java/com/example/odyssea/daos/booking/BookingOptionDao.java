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
        String sql = "SELECT options.* FROM options \n" +
                "INNER JOIN booking_option ON options.id = booking_option.option_id \n" +
                "INNER JOIN booking ON booking_option.booking_id = booking.id\n" +
                "WHERE booking.id = ?";

        return jdbcTemplate.query(sql, new Object[]{bookingId}, new BeanPropertyRowMapper<>(Option.class));
    }

    // Ajouter une option d'une réservation
    public void insertBooking(int userId, int itineraryId, int optionId) {
        String sql = "INSERT INTO booking_option (user_id, itinerary_id, option_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, itineraryId, optionId);
    }

    //Supprimer une option d'une réservation
    public void deleteOptionFromBooking(int userId, int itineraryId, int optionId) {
        String sql = "DELETE FROM booking_option WHERE user_id = ? AND itinerary_id = ? AND option_id = ?";
        jdbcTemplate.update(sql, userId, itineraryId, optionId);
    }

    public void insertBookingOption(int bookingId, int optionId) {
        String sql = "INSERT INTO booking_option (booking_id, option_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, bookingId, optionId);
    }

    /** Supprime toutes les options associées à une réservation donnée.*/
    public void deleteOptionsForBooking(int bookingId) {
        String sql = "DELETE FROM booking_option WHERE booking_id = ?";
        jdbcTemplate.update(sql, bookingId);}
}
