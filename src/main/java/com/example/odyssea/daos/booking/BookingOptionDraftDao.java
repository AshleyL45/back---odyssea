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
            rs.getInt("booking_draft_id"),
            rs.getInt("option_id")
    );

    /**
     * Récupère la liste des options associées à un draft.
     */
    public List<Option> getOptionsByDraftId(int draftId) {
        String sql = """
            SELECT o.* 
              FROM booking_options_draft bod
              JOIN options o ON bod.option_id = o.id
             WHERE bod.booking_draft_id = ?
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Option.class), draftId);
    }

    /**
     * Enregistre la liste d'options pour un draft.
     */
    public void saveOptions(int draftId, List<Integer> optionIds) {
        String sql = "INSERT INTO booking_options_draft (booking_draft_id, option_id) VALUES (?, ?)";
        try {
            for (Integer optionId : optionIds) {
                jdbcTemplate.update(sql, draftId, optionId);
            }
        } catch (Exception e) {
            throw new DatabaseException("Error saving draft options: " + e.getMessage());
        }
    }

    /**
     * Supprime toutes les options liées à un draft (silent delete).
     */
    public void deleteOptionsByDraftId(int draftId) {
        String sql = "DELETE FROM booking_options_draft WHERE booking_draft_id = ?";
        jdbcTemplate.update(sql, draftId);
        // on ne lève pas d'exception si 0 lignes supprimées
    }
}
