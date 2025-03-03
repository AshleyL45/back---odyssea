package com.example.odyssea.daos.userItinerary;

import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class UserItineraryStepDao {
    private final JdbcTemplate jdbcTemplate;

    public UserItineraryStepDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserItineraryStep> userItineraryStepRowMapper = (rs, _) -> new UserItineraryStep(
            rs.getInt("id"),
            rs.getInt("userId"),
            rs.getInt("userItineraryId"),
            rs.getInt("hotelId"),
            rs.getInt("cityId"),
            rs.getInt("position"),
            rs.getInt("dayNumber"),
            rs.getBoolean("offDay")
    );

    public List<UserItineraryStep> findAll (){
        String sql = "SELECT * FROM userItineraryStep";
        return jdbcTemplate.query(sql, userItineraryStepRowMapper);
    }

    public UserItineraryStep findById(int id){
        String sql = "SELECT * FROM userItineraryStep WHERE id = ?";
        return jdbcTemplate.query(sql, userItineraryStepRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The user itinerary step id " + id + "you are looking for does not exist."));
    }

    public LocalDate convertDayNumberToDate(int dayNumber, LocalDate startDate){
       return startDate.plusDays(dayNumber - 1);
    }

    public LocalDate getStartDateForItinerary(int itineraryId) {
        String sql = "SELECT startDate FROM userItinerary WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, LocalDate.class, itineraryId);
    }

    public UserItineraryStep save (UserItineraryStep userItineraryStep){
        String sql = "INSERT INTO userItineraryStep (userId, userItineraryId, hotelId, cityId, position, dayNumber, offDay) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userItineraryStep.getUserId(), userItineraryStep.getUserItineraryId(), userItineraryStep.getHotelId(), userItineraryStep.getCityId(), userItineraryStep.getPosition(), userItineraryStep.getDayNumber(), userItineraryStep.isOffDay());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        userItineraryStep.setId(id);
        return userItineraryStep;
    }

    public UserItineraryStep update(int id, UserItineraryStep userItineraryStep){
        if(!userItineraryStepExists(id)){
            throw new RuntimeException("The user itinerary step you are looking for does not exist.");
        }

        String sql = "UPDATE userItineraryStep SET userId = ?, userItineraryId = ?, hotelId = ?, cityId = ?, position = ?, dayNumber = ?, offDay = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItineraryStep.getUserId(), userItineraryStep.getUserItineraryId(), userItineraryStep.getHotelId(), userItineraryStep.getCityId(), userItineraryStep.getPosition(), userItineraryStep.getDayNumber(), userItineraryStep.isOffDay(), id);

        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the user itinerary step with id : " + id);
        }

        return this.findById(id);

    }

    public boolean delete(int id) {
        String sql = "DELETE FROM userItineraryStep WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }


    public boolean userItineraryStepExists(int id){
        String sqlCheck = "SELECT COUNT(*) FROM userItineraryStep WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, id);
        return count > 0;
    }
}
