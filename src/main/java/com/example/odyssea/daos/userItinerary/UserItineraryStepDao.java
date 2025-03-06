package com.example.odyssea.daos.userItinerary;

import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserItineraryStepDao {
    private final JdbcTemplate jdbcTemplate;

    public UserItineraryStepDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserItineraryStep> userItineraryStepRowMapper = (rs, _) -> new UserItineraryStep(
            rs.getInt("userId"),
            rs.getInt("userItineraryId"),
            rs.getInt("hotelId"),
            rs.getInt("cityId"),
            rs.getInt("dayNumber"),
            rs.getBoolean("offDay"),
            rs.getInt("activityId"),
            rs.getInt("flightId")
    );

    public List<UserItineraryStep> findAll (){
        String sql = "SELECT * FROM userDailyPlan";
        return jdbcTemplate.query(sql, userItineraryStepRowMapper);
    }

    public List<UserItineraryStep> findDailyPlansOfAnItinerary(int itineraryId){
        String sql = "SELECT * FROM userDailyPlan WHERE userItineraryId = ?";
        return jdbcTemplate.query(sql, userItineraryStepRowMapper, itineraryId);
    }

    public UserItineraryStep findById(int userId, int userItineraryId, int dayNumber){
        String sql = "SELECT * FROM userDailyPlan  WHERE userId = ? AND userItineraryId = ? AND dayNumber = ?";
        return jdbcTemplate.query(sql, userItineraryStepRowMapper, userId, userItineraryId, dayNumber)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The daily plan of the (user) itinerary " + userItineraryId + " day " + dayNumber + " you are looking for does not exist."));
    }

    /*public LocalDate convertDayNumberToDate(int dayNumber, LocalDate startDate){
       return startDate.plusDays(dayNumber - 1);
    }

    public LocalDate getStartDateForItinerary(int itineraryId) {
        String sql = "SELECT startDate FROM userDailyPlan WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, LocalDate.class, itineraryId);
    }*/

    public UserItineraryStep save (UserItineraryStep userItineraryStep){
        String sql = "INSERT INTO userDailyPlan (userId, userItineraryId, hotelId, cityId, dayNumber, offDay, activityId, flightId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userItineraryStep.getUserId(), userItineraryStep.getUserItineraryId(), userItineraryStep.getHotelId(), userItineraryStep.getCityId(), userItineraryStep.getDayNumber(), userItineraryStep.isOffDay(), userItineraryStep.getActivityId(), userItineraryStep.getActivityId());

        return userItineraryStep;
    }

    public List<Hotel> getHotelInADay(int userItineraryId, int dayNumber){
        String sql = "SELECT hotel.* FROM userDailyPlan \n" +
                "INNER JOIN hotel ON userDailyPlan.hotelId = hotel.id\n" +
                "WHERE userItineraryId = ? AND dayNumber = ?;";
        return jdbcTemplate.query(sql, new Object[]{userItineraryId, dayNumber}, new BeanPropertyRowMapper<>(Hotel.class));
    }

    public List<Activity> getActivitiesInADay(int userItineraryId, int dayNumber){
        String sql = "SELECT activity.* FROM userDailyPlan \n" +
                "INNER JOIN activity ON userDailyPlan.activityId = activity.id\n" +
                "WHERE userItineraryId = ? AND dayNumber = ?;";
        return jdbcTemplate.query(sql, new Object[]{userItineraryId, dayNumber}, new BeanPropertyRowMapper<>(Activity.class));
    }

    public UserItineraryStep update(int userId, int userItineraryId, int dayNumber, UserItineraryStep userItineraryStep){
        if(!userItineraryStepExists(userId, userItineraryId, dayNumber)){
            throw new RuntimeException("The daily itinerary you are looking for does not exist.");
        }

        String sql = "UPDATE userDailyPlan SET userId = ?, userItineraryId = ?, hotelId = ?, cityId = ?, dayNumber = ?, offDay = ?, activityId = ?, flightId = ? WHERE userId = ? AND userItineraryId = ? AND dayNumber = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItineraryStep.getUserId(), userItineraryStep.getUserItineraryId(), userItineraryStep.getHotelId(), userItineraryStep.getCityId(), userItineraryStep.getDayNumber(), userItineraryStep.isOffDay(), userItineraryStep.getActivityId(), userItineraryStep.getFlightId(), userId, userItineraryId, dayNumber);

        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the daily plan with id from the (user) itinerary : " + userItineraryId + " day number " + dayNumber + ".");
        }

        return this.findById(userId, userItineraryId, dayNumber);

    }

    public boolean delete(int userId, int userItineraryId, int dayNumber) {
        String sql = "DELETE FROM userDailyPlan WHERE userId = ? AND userItineraryId = ? AND dayNumber = ?";
        int rowsAffected = jdbcTemplate.update(sql, userId, userItineraryId, dayNumber);
        return rowsAffected > 0;
    }


    public boolean userItineraryStepExists(int userId, int userItineraryId, int dayNumber ){
        String sqlCheck = "SELECT COUNT(*) FROM userDailyPlan WHERE userId = ? AND userItineraryId = ? AND dayNumber = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, userId, userItineraryId, dayNumber);
        return count > 0;
    }
}
