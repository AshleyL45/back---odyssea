package com.example.odyssea.daos.booking;

import com.example.odyssea.entities.booking.BookingDraft;
import com.example.odyssea.exceptions.DatabaseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class BookingDraftDao {

    private final JdbcTemplate jdbcTemplate;

    public BookingDraftDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BookingDraft> bookingRowMapper = (rs, rowNum) -> new BookingDraft(
            rs.getInt("draft_id"),
            rs.getObject("user_id", Integer.class),
            rs.getObject("itinerary_id", Integer.class),
            rs.getDate("departure_date") != null ? rs.getDate("departure_date").toLocalDate() : null,
            rs.getObject("number_of_adults", Integer.class),
            rs.getObject("number_of_kids", Integer.class),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getString("type")
    );

    public Integer getLastDraftIdByUser(int userId) {
        String sql = "SELECT draft_id FROM booking_draft WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userId);
        } catch (EmptyResultDataAccessException e) {
            // No draft exists, create a new one
            return createInitialDraft(userId);
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving draft: " + e.getMessage());
        }
    }

    private Integer createInitialDraft(int userId) {
        String insertSql = "INSERT INTO booking_draft (user_id, created_at) VALUES (?, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new DatabaseException("Failed to create initial draft for user " + userId);
        }
        return key.intValue();
    }

    public BookingDraft getLastDraftByUserId(int userId) {
        int draftId = getLastDraftIdByUser(userId);
        String sql = "SELECT * FROM booking_draft WHERE draft_id = ?";
        return jdbcTemplate.query(sql, bookingRowMapper, draftId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new DatabaseException("No draft found for user with ID: " + userId));
    }

    public void updateItineraryId(int draftId, int itineraryId) {
        String sql = "UPDATE booking_draft SET itinerary_id = ? WHERE draft_id = ?";
        int rows = jdbcTemplate.update(sql, itineraryId, draftId);
        if (rows == 0) throw new DatabaseException("No draft found with ID: " + draftId);
    }

    public void updateDepartureDate(int draftId, LocalDate departureDate) {
        String sql = "UPDATE booking_draft SET departure_date = ? WHERE draft_id = ?";
        int rows = jdbcTemplate.update(sql, Date.valueOf(departureDate), draftId);
        if (rows == 0) throw new DatabaseException("No draft found with ID: " + draftId);
    }

    public void updateNumberOfAdults(int draftId, int numberOfAdults) {
        String sql = "UPDATE booking_draft SET number_of_adults = ? WHERE draft_id = ?";
        int rows = jdbcTemplate.update(sql, numberOfAdults, draftId);
        if (rows == 0) throw new DatabaseException("No draft found with ID: " + draftId);
    }

    public void updateNumberOfKids(int draftId, int numberOfKids) {
        String sql = "UPDATE booking_draft SET number_of_kids = ? WHERE draft_id = ?";
        int rows = jdbcTemplate.update(sql, numberOfKids, draftId);
        if (rows == 0) throw new DatabaseException("No draft found with ID: " + draftId);
    }

    public void updateType(int draftId, String type) {
        String sql = "UPDATE booking_draft SET type = ? WHERE draft_id = ?";
        int rows = jdbcTemplate.update(sql, type, draftId);
        if (rows == 0) {
            throw new DatabaseException("No draft found with ID: " + draftId);
        }
    }

    public List<Integer> getOptionIdsByDraftId(int draftId) {
        String sql = "SELECT option_id FROM booking_options_draft WHERE booking_draft_id = ?";
        List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class, draftId);
        if (ids.isEmpty()) {
            throw new DatabaseException("No options found for draft ID: " + draftId);
        }
        return ids;
    }

    public void deleteOptionsByDraftId(int draftId) {
        String sql = "DELETE FROM booking_options_draft WHERE booking_draft_id = ?";
        jdbcTemplate.update(sql, draftId);
    }

    public void saveOptions(int draftId, List<Integer> optionIds) {
        String sql = "INSERT INTO booking_options_draft (booking_draft_id, option_id) VALUES (?, ?)";
        for (Integer optId : optionIds) {
            jdbcTemplate.update(sql, draftId, optId);
        }
    }
    }
