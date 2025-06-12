package com.example.odyssea.daos.booking;

import com.example.odyssea.entities.booking.BookingDraft;
import com.example.odyssea.exceptions.DatabaseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;

@Repository
public class BookingDraftDao {

    private final JdbcTemplate jdbcTemplate;

    public BookingDraftDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BookingDraft> booking = (rs, rowNum) -> new BookingDraft(
            rs.getInt("draft_id"),
            rs.getObject("user_id", Integer.class),
            rs.getObject("itinerary_id", Integer.class),
            rs.getDate("departure_date").toLocalDate(),
            rs.getInt("number_of_adults"),
            rs.getInt("number_of_kids"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getString("type")
    );

    public BookingDraft getLastDraftByUserId(Integer userId) {
        String sql = "SELECT * FROM booking WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
        return jdbcTemplate.query(sql, booking, userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new DatabaseException("No draft found for user with ID : " + userId));
    }

    public Integer getLastDraftIdByUser(int userId) {
        try {
            String sql = "SELECT draft_id FROM booking WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
            return jdbcTemplate.queryForObject(sql, Integer.class, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new DatabaseException("No draft found for user with ID: " + userId);
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving draft: " + e.getMessage());
        }
    }

    public void updateItineraryId(int draftId, int itineraryId) {
        String sql = "UPDATE booking SET itinerary_id = ? WHERE draft_id = ?";
        int rows = jdbcTemplate.update(sql, itineraryId, draftId);
        if (rows == 0) throw new DatabaseException("No draft found with ID: " + draftId);
    }


    public void updateDepartureDate(int draftId, LocalDate departureDate) {
        String sql = "UPDATE booking SET departure_date = ? WHERE draft_id = ?";
        int rows = jdbcTemplate.update(sql, Date.valueOf(departureDate), draftId);
        if (rows == 0) throw new DatabaseException("No draft found with ID: " + draftId);
    }

    public void updateNumberOfAdults(int draftId, int numberOfAdults) {
        String sql = "UPDATE booking SET number_of_adults = ? WHERE draft_id = ?";
        int rows = jdbcTemplate.update(sql, numberOfAdults, draftId);
        if (rows == 0) throw new DatabaseException("No draft found with ID: " + draftId);
    }

    public void updateNumberOfKids(int draftId, int numberOfKids) {
        String sql = "UPDATE booking SET number_of_kids = ? WHERE draft_id = ?";
        int rows = jdbcTemplate.update(sql, numberOfKids, draftId);
        if (rows == 0) throw new DatabaseException("No draft found with ID: " + draftId);
    }

    public void updateType(int draftId, String type) {
        String sql = "UPDATE booking SET type = ? WHERE draft_id = ?";
        int rows = jdbcTemplate.update(sql, type, draftId);
        if (rows == 0) {
            throw new DatabaseException("No draft found with ID: " + draftId);
        }
    }

}
