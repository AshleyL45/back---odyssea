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

    /**
     * Récupère la liste des options associées à une réservation donnée.
     */
    public List<Option> getBookingOptions(int bookingId) {
        String sql = """
            SELECT o.*
              FROM options o
              JOIN booking_option bo
                ON bo.option_id = o.id
             WHERE bo.booking_id = ?
            """;
        return jdbcTemplate.query(
                sql,
                new Object[]{bookingId},
                new BeanPropertyRowMapper<>(Option.class)
        );
    }

    /**
     * Insère une option pour une réservation donnée.
     */
    public void insertBookingOption(int bookingId, int optionId) {
        String sql = "INSERT INTO booking_option (booking_id, option_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, bookingId, optionId);
    }

    /**
     * Supprime toutes les options associées à une réservation donnée.
     */
    public void deleteOptionsForBooking(int bookingId) {
        String sql = "DELETE FROM booking_option WHERE booking_id = ?";
        jdbcTemplate.update(sql, bookingId);
    }
}
