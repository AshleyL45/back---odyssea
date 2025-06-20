package com.example.odyssea.daos.userItinerary;

import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDailyPlanDao {
    private static final Logger log = LoggerFactory.getLogger(UserDailyPlanDao.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDailyPlanDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserItineraryStep> userItineraryStepRowMapper = (rs, _) -> new UserItineraryStep(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getInt("user_itinerary_id"),
            rs.getInt("hotel_id"),
            rs.getInt("city_id"),
            rs.getInt("day_number"),
            rs.getBoolean("off_day"),
            rs.getInt("activity_id"),
            rs.getInt("plane_ride_id")
    );

    public List<UserItineraryStep> findAll (){
        String sql = "SELECT * FROM user_daily_plan";
        return jdbcTemplate.query(sql, userItineraryStepRowMapper);
    }

    public List<UserItineraryStep> findDailyPlansOfAnItinerary(int itineraryId){
        String sql = "SELECT * FROM user_daily_plan WHERE user_itinerary_id = ?";
        return jdbcTemplate.query(sql, userItineraryStepRowMapper, itineraryId);
    }

    public UserItineraryStep findById(int userId, int userItineraryId, int dayNumber){
        String sql = "SELECT * FROM user_daily_plan  WHERE user_id = ? AND user_itinerary_id = ? AND day_number = ?";
        return jdbcTemplate.query(sql, userItineraryStepRowMapper, userId, userItineraryId, dayNumber)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The daily plan of the (user) itinerary " + userItineraryId + " day " + dayNumber + " you are looking for does not exist."));
    }

    public List<UserItineraryStep> findByUserId(int userId){
        String sql = "SELECT * FROM user_daily_plan WHERE user_id = ?";
        return jdbcTemplate.query(sql, userItineraryStepRowMapper, userId);
    }


    public UserItineraryStep save (UserItineraryStep userItineraryStep){
        String sql = "INSERT INTO user_daily_plan (user_id, user_itinerary_id, hotel_id, city_id, day_number, off_day, activity_id, plane_ride_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userItineraryStep.getUserId(), userItineraryStep.getUserItineraryId(), userItineraryStep.getHotelId(), userItineraryStep.getCityId(), userItineraryStep.getDayNumber(), userItineraryStep.isOffDay(), userItineraryStep.getActivityId(), userItineraryStep.getPlaneRideId());

        return userItineraryStep;
    }

    public Hotel getHotelInADay(int userItineraryId, int dayNumber){
        String sql = "SELECT hotel.* FROM user_daily_plan \n" +
                "INNER JOIN hotel ON user_daily_plan.hotel_id = hotel.id\n" +
                "WHERE user_itinerary_id = ? AND day_number = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userItineraryId, dayNumber}, new BeanPropertyRowMapper<>(Hotel.class));
    }

    public Activity getActivityInADay(int userItineraryId, int dayNumber){
        String sql = "SELECT activity.* FROM user_daily_plan \n" +
                "INNER JOIN activity ON user_daily_plan.activity_id = activity.id\n" +
                "WHERE user_itinerary_id = ? AND day_number = ?";


        List<Activity> activities = jdbcTemplate.query(sql, new Object[]{userItineraryId, dayNumber}, new BeanPropertyRowMapper<>(Activity.class));
        return activities.isEmpty() ? null : activities.get(0);
    }

    public UserItineraryStep update(int userId, int userItineraryId, int dayNumber, UserItineraryStep userItineraryStep){
        if(!userItineraryStepExists(userId, userItineraryId, dayNumber)){
            throw new RuntimeException("The daily itinerary you are looking for does not exist.");
        }

        String sql = "UPDATE user_daily_plan SET user_id = ?, user_itinerary_id = ?, hotel_id = ?, city_id = ?, day_number = ?, off_day = ?, activity_id = ?, plane_ride_id = ? WHERE user_id = ? AND user_itinerary_id = ? AND day_number = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItineraryStep.getUserId(), userItineraryStep.getUserItineraryId(), userItineraryStep.getHotelId(), userItineraryStep.getCityId(), userItineraryStep.getDayNumber(), userItineraryStep.isOffDay(), userItineraryStep.getActivityId(), userItineraryStep.getPlaneRideId(), userId, userItineraryId, dayNumber);

        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the daily plan with id from the (user) itinerary : " + userItineraryId + " day number " + dayNumber + ".");
        }

        return this.findById(userId, userItineraryId, dayNumber);

    }

    public boolean delete(int userId, int userItineraryId, int dayNumber) {
        String sql = "DELETE FROM user_daily_plan WHERE user_id = ? AND user_itinerary_id = ? AND day_number = ?";
        int rowsAffected = jdbcTemplate.update(sql, userId, userItineraryId, dayNumber);
        return rowsAffected > 0;
    }


    public boolean userItineraryStepExists(int userId, int userItineraryId, int dayNumber ){
        String sqlCheck = "SELECT COUNT(*) FROM user_daily_plan WHERE user_id = ? AND user_itinerary_id = ? AND day_number = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, userId, userItineraryId, dayNumber);
        return count > 0;
    }
}
