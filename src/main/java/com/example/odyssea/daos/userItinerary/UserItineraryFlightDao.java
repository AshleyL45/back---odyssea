package com.example.odyssea.daos.userItinerary;

import com.example.odyssea.entities.userItinerary.UserItineraryFlight;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserItineraryFlightDao {
    private final JdbcTemplate jdbcTemplate;

    public UserItineraryFlightDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserItineraryFlight> userItineraryFlightRowMapper = (rs, _) -> new UserItineraryFlight(
            rs.getInt("userItineraryId"),
            rs.getInt("flightId")
    );

    public List<UserItineraryFlight> findAll (){
        String sql = "SELECT * FROM userItineraryFlight";
        return jdbcTemplate.query(sql, userItineraryFlightRowMapper);
    }

    public List<UserItineraryFlight> findByUserItinerary(int userItineraryId){ // Gets flights of a user itinerary
        String sql = "SELECT * FROM userItineraryFlight WHERE userItineraryId = ?";
        return jdbcTemplate.query(sql, userItineraryFlightRowMapper, userItineraryId);
    }

    public UserItineraryFlight findByIds(int userItineraryId, int flightId){ // Find a flight of a user itinerary
        String sql = "SELECT * FROM userItineraryFlight WHERE userItineraryId = ? AND flightId = ?";
        return jdbcTemplate.query(sql, userItineraryFlightRowMapper, userItineraryId, flightId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The user itinerary flight you are looking for doesn't exists."));
    }

    public UserItineraryFlight save (UserItineraryFlight userItineraryFlight){
        String sql = "INSERT INTO userItineraryFlight (userItineraryId, flightId) VALUES (?, ?)";
        jdbcTemplate.update(sql, userItineraryFlight.getUserItineraryId(), userItineraryFlight.getFlightId());

        return userItineraryFlight;
    }

    public UserItineraryFlight update(int userItineraryId, int flightId, UserItineraryFlight userItineraryFlight){
        if(!userItineraryFlightExists(userItineraryId, flightId)){
            throw new RuntimeException("The user itinerary flight you are looking for does not exist.");
        }

        String sql = "UPDATE userItineraryFlight SET userItineraryId = ?, flightId = ? WHERE userItineraryId = ? AND flightId = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItineraryFlight.getUserItineraryId(), userItineraryFlight.getFlightId(), userItineraryId, flightId);

        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the user itinerary flight with id : " + flightId);
        }

        return this.findByIds(userItineraryId, flightId);

    }

    public boolean delete(int userItineraryId, int flightId) {
        String sql = "DELETE FROM userItineraryFlight WHERE userItineraryId = ? AND flightId = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItineraryId, flightId);
        return rowsAffected > 0;
    }


    public boolean userItineraryFlightExists(int userItineraryId, int flightId){
        String sqlCheck = "SELECT COUNT(*) FROM userItineraryFlight WHERE userItineraryId = ? AND flightId = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, userItineraryId, flightId);
        return count > 0;
    }

}
