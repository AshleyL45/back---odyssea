package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.ReservationOptionDraft;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.exceptions.DatabaseException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationOptionDraftDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationOptionDraftDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationOptionDraft> optionRowMapper = (rs, rowNum) -> new ReservationOptionDraft(
            rs.getInt("reservation_draft_id"),
            rs.getInt("option_id")
    );

    public List<Option> getOptionsByDraftId(int draftId) {
        String sql = "SELECT options.* FROM reservation_options_draft INNER JOIN options ON reservation_options_draft.option_id = options.id WHERE reservation_draft_id = ?";
        return jdbcTemplate.query(sql, new Object[]{draftId}, new BeanPropertyRowMapper<>(Option.class));
    }

    public void saveOptions(int draftId, List<Integer> optionIds) {
        try {
            String sql = "INSERT INTO reservation_options_draft (reservation_draft_id, option_id) VALUES (?, ?)";

            for (Integer optionId : optionIds) {
                jdbcTemplate.update(sql, draftId, optionId);
            }
        } catch (Exception e) {
            throw new DatabaseException("An error occurred while saving draft options: " + e.getMessage());
        }
    }

    public void deleteOptionsByDraftId(int draftId) {
        String sql = "DELETE FROM reservation_options_draft WHERE reservation_draft_id = ?";
        jdbcTemplate.update(sql, draftId);
    }
}
