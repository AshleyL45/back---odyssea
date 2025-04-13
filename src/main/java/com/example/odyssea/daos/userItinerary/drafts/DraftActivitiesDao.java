package com.example.odyssea.daos.userItinerary.drafts;

import com.example.odyssea.entities.userItinerary.drafts.DraftActivities;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DraftActivitiesDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserItineraryDraftDao userItineraryDraftDao;

    public DraftActivitiesDao(JdbcTemplate jdbcTemplate, UserItineraryDraftDao userItineraryDraftDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userItineraryDraftDao = userItineraryDraftDao;
    }

    private final RowMapper<DraftActivities> draftActivitiesRowMapper = (rs, _) -> new DraftActivities(
            rs.getInt("draft_user_itinerary_id"),
            rs.getInt("activity_id"),
            rs.getInt("position")
    );

    public void saveActivities(Integer userId, List<Integer> activitiesIds){
        Integer draftId = userItineraryDraftDao.getLastDraftIdByUser(userId);
        String sql = "INSERT INTO draft_activities (draft_user_itinerary_id, activity_id, position) VALUES (?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < activitiesIds.size(); i++) {
            batchArgs.add(new Object[] {draftId, activitiesIds.get(i), i + 1});
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
