package com.example.odyssea.daos.userItinerary;

import com.example.odyssea.entities.userItinerary.UserItinerary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.Duration;
import java.util.List;

public class UserItineraryDao {

    private final JdbcTemplate jdbcTemplate;

    public UserItineraryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserItinerary> userItineraryRowMapper = (rs, _) -> new UserItinerary(
            rs.getInt("id"),
            rs.getInt("userId"),
            rs.getDate("startDate"),
            rs.getDate("endDate"),
            rs.getInt("numberOfPeople"),
            rs.getBigDecimal("startingPrice"),
            Duration.ofMillis(rs.getLong("totalDuration"))
    );

    public List<UserItinerary> findAll (){
        String sql = "SELECT * FROM userItinerary";
        return jdbcTemplate.query(sql, userItineraryRowMapper);
    }

    public UserItinerary findById(int id){
        String sql = "SELECT * FROM userItinerary WHERE id = ?";
        return jdbcTemplate.query(sql, userItineraryRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The user itinerary id " + id + "you are looking for does not exist."));
    }

    public UserItinerary save (UserItinerary userItinerary){
        String sql = "INSERT INTO userItinerary (userId, startDate, endDate, numberOfPeople, startingPrice, totalDuration) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userItinerary.getUserId(), userItinerary.getStartDate(), userItinerary.getEndDate(), userItinerary.getNumberOfPeople(), userItinerary.getStartingPrice(), userItinerary.getTotalDuration());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        userItinerary.setId(id);
        return userItinerary;
    }

    public UserItinerary update(int id, UserItinerary userItinerary){
        if(!userItineraryExists(id)){
            throw new RuntimeException("The user itinerary you are looking for does not exist.");
        }

        String sql = "UPDATE userItinerary SET userId = ?, startDate = ?, endDate = ?, numberOfPeople = ?, startingPrice = ?, totalDuration = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItinerary.getUserId(), userItinerary.getStartDate(), userItinerary.getEndDate(), userItinerary.getNumberOfPeople(), userItinerary.getStartingPrice(), userItinerary.getTotalDuration(), id);

        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the user itinerary with id : " + id);
        }

        return this.findById(id);

    }

    public boolean deleteUserItineraryInfo (int id){ // Deletes all information related with this user itinerary
        String sql = "DELETE FROM userItinerary WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        String sqlSteps = "DELETE FROM userItineraryStep WHERE userItineraryId = ?";
        int rowsAffectedSteps = jdbcTemplate.update(sqlSteps, id);

        String sqlOptions = "DELETE FROM userItineraryOption WHERE userItineraryId = ?";
        int rowsAffectedOptions = jdbcTemplate.update(sqlOptions, id);

        String sqlFlights = "DELETE FROM userItineraryFlight WHERE userItineraryId = ?";
        int rowsAffectedFlights = jdbcTemplate.update(sqlFlights, id);

        String sqlActivities = "DELETE FROM userItineraryActivitiesPerDay WHERE userItineraryStepId IN (SELECT id FROM userItineraryStep WHERE userItineraryId = ?)";
        int rowsActivities = jdbcTemplate.update(sqlActivities, id);

        return rowsAffected > 0 && rowsAffectedSteps > 0 && rowsAffectedOptions > 0 && rowsAffectedFlights > 0 && rowsActivities > 0;
    }

    public boolean userItineraryExists(int id){
        String sqlCheck = "SELECT COUNT(*) FROM userItinerary WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, id);
        return count > 0;
    }
}
