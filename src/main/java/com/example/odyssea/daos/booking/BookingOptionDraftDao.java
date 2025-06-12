package com.example.odyssea.daos.booking;

import com.example.odyssea.entities.booking.BookingOptionDraft;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.exceptions.DatabaseException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingOptionDraftDao {

    private final JdbcTemplate jdbcTemplate;

    public BookingOptionDraftDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BookingOptionDraft> optionRowMapper = (rs, rowNum) -> new BookingOptionDraft(
            rs.getInt("booking"),
            rs.getInt("option_id")
    );

    public List<Option> getOptionsByDraftId(int draftId) {
        String sql = "SELECT options.* FROM booking INNER JOIN options ON booking.option_id = options.id WHERE booking = ?";
        return jdbcTemplate.query(sql, new Object[]{draftId}, new BeanPropertyRowMapper<>(Option.class));
    }

    public void saveOptions(int draftId, List<Integer> optionIds) {
        try {
            String sql = "INSERT INTO booking (booking, option_id) VALUES (?, ?)";

            for (Integer optionId : optionIds) {
                jdbcTemplate.update(sql, draftId, optionId);
            }
        } catch (Exception e) {
            throw new DatabaseException("An error occurred while saving draft options: " + e.getMessage());
        }
    }

    public void deleteOptionsByDraftId(int draftId) {
        String sql = "DELETE FROM booking WHERE booking = ?";
        jdbcTemplate.update(sql, draftId);
    }
}
