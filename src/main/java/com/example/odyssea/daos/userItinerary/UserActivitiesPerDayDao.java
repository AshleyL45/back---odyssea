package com.example.odyssea.daos.userItinerary;

import com.example.odyssea.entities.userItinerary.UserActivitiesPerDay;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class UserActivitiesPerDayDao {
    private final JdbcTemplate jdbcTemplate;

    public UserActivitiesPerDayDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserActivitiesPerDay> userActivitiesPerDayRowMapper = (rs, _) -> new UserActivitiesPerDay(
            rs.getInt("userItineraryStepId"),
            rs.getInt("activityId"),
            rs.getInt("dayNumber")
    );

    public List<UserActivitiesPerDay> findAll (){ // Gets all activity of all user itineraries
        String sql = "SELECT * FROM userItineraryActivitiesPerDay";
        return jdbcTemplate.query(sql, userActivitiesPerDayRowMapper);
    }

    public List<UserActivitiesPerDay> findActivitiesByUserItineraryStepId(int userItineraryStepId){ // Gets all activities of a user itinerary
        String sql = "SELECT * FROM userItineraryActivitiesPerDay WHERE userItineraryStepId = ?";
        return jdbcTemplate.query(sql, userActivitiesPerDayRowMapper, userItineraryStepId);
    }

    public List<UserActivitiesPerDay> findActivitiesInADay(int userItineraryStepId, int dayNumber){ // Gets all activities in a day of a user itinerary
        String sql = "SELECT * FROM userItineraryActivitiesPerDay WHERE userItineraryStepId = ? AND dayNumber = ?";
        return jdbcTemplate.query(sql, userActivitiesPerDayRowMapper, userItineraryStepId, dayNumber);
    }

    public UserActivitiesPerDay save (UserActivitiesPerDay userActivitiesPerDay){
        String sql = "INSERT INTO userItineraryActivitiesPerDay (userItineraryStepId, activityId, dayNumber) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userActivitiesPerDay.getUserItineraryStepId(), userActivitiesPerDay.getActivityId(), userActivitiesPerDay.getDayNumber());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        userActivitiesPerDay.setUserItineraryStepId(id);
        return userActivitiesPerDay;
    }

    public UserActivitiesPerDay update(int userItineraryStepId, int dayNumber, UserActivitiesPerDay userActivitiesPerDay){
        if(!userActivitiesPerDayExists(userItineraryStepId, dayNumber)){
            throw new RuntimeException("There are no activities for the day " + dayNumber + "for the user itinerary id " + userItineraryStepId);
        }

        String sql = "UPDATE userItineraryActivitiesPerDay SET activityId = ?, dayNumber = ? WHERE userItineraryStepId = ? AND dayNumber = ?";
        int rowsAffected = jdbcTemplate.update(sql, userActivitiesPerDay.getActivityId(), userActivitiesPerDay.getDayNumber(), userItineraryStepId, dayNumber);

        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the activities for itinerary step ID: " + userItineraryStepId);
        }

        return this.findActivitiesInADay(userItineraryStepId, dayNumber).getFirst();

    }

    public boolean delete(int userItineraryStepId, int dayNumber) {
        String sql = "DELETE FROM userItineraryActivitiesPerDay WHERE userItineraryStepId = ? AND dayNumber = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItineraryStepId, dayNumber);
        return rowsAffected > 0;
    }

    public boolean userActivitiesPerDayExists(int userItineraryStepId,int dayNumber){ // Checks if there are activities in a day of a user itinerary
        String sqlCheck = "SELECT COUNT(*) FROM userItineraryActivitiesPerDay WHERE userItineraryStepId = ? AND dayNumber = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, userItineraryStepId, dayNumber);
        return count > 0;
    }
}
