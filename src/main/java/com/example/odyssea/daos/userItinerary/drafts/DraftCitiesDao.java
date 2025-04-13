package com.example.odyssea.daos.userItinerary.drafts;

import com.example.odyssea.entities.userItinerary.drafts.DraftCities;
import com.example.odyssea.entities.userItinerary.drafts.DraftOptions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DraftCitiesDao {
    private final JdbcTemplate jdbcTemplate;

    public DraftCitiesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<DraftCities> draftOptionsRowMapper = (rs, _) -> new DraftCities(
            rs.getInt("draft_user_itinerary_id"),
            rs.getInt("city_id")
    );
}
