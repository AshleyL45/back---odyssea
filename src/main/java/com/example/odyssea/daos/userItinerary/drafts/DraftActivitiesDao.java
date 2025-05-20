package com.example.odyssea.daos.userItinerary.drafts;

import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.userItinerary.drafts.DraftActivities;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

        deleteActivitiesByDraftId(draftId);
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < activitiesIds.size(); i++) {
            batchArgs.add(new Object[] {draftId, activitiesIds.get(i), i + 1});
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public List<Activity> findDraftActivities(Integer userId){
        Integer draftId = userItineraryDraftDao.getLastDraftIdByUser(userId);
        String sql = "SELECT activity.* FROM draft_activities\n" +
                "INNER JOIN activity ON draft_activities.activity_id = activity.id WHERE draft_activities.draft_user_itinerary_id = ? ";

        return jdbcTemplate.query(sql, new Object[]{draftId}, new BeanPropertyRowMapper<>(Activity.class));
    }

    private void deleteActivitiesByDraftId(Integer draftId) {
        String deleteSql = "DELETE FROM draft_activities WHERE draft_user_itinerary_id = ?";
        jdbcTemplate.update(deleteSql, draftId);
    }
}
