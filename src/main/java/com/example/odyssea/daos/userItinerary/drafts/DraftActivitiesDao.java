package com.example.odyssea.daos.userItinerary.drafts;

import com.example.odyssea.entities.userItinerary.drafts.DraftActivities;
import com.example.odyssea.entities.userItinerary.drafts.DraftOptions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DraftActivitiesDao {
    private final JdbcTemplate jdbcTemplate;

    public DraftActivitiesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<DraftActivities> draftActivitiesRowMapper = (rs, _) -> new DraftActivities(
            rs.getInt("draft_user_itinerary_id"),
            rs.getInt("activity_id")
    );
}
