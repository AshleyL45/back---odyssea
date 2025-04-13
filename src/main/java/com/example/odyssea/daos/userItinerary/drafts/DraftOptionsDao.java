package com.example.odyssea.daos.userItinerary.drafts;

import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.userItinerary.drafts.DraftOptions;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public class DraftOptionsDao {
    private final JdbcTemplate jdbcTemplate;

    public DraftOptionsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<DraftOptions> draftOptionsRowMapper = (rs, _) -> new DraftOptions(
            rs.getInt("draft_user_itinerary_id"),
            rs.getInt("option_id")
    );

    public List<Option> findAllByUserItineraryId(Integer useItineraryId){
        String sql = "SELECT options.* FROM draft_options INNER JOIN options ON draft_options.option_id = options.id";
        return jdbcTemplate.query(sql, new Object[]{useItineraryId}, new BeanPropertyRowMapper<>(Option.class));
    }
}
