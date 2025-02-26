package com.example.odyssea.daos.userItinerary;

import com.example.odyssea.entities.userItinerary.UserItineraryOption;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class UserItineraryOptionDao {
    private final JdbcTemplate jdbcTemplate;


    public UserItineraryOptionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserItineraryOption> userItineraryOptionRowMapper = (rs, _) -> new UserItineraryOption(
            rs.getInt("userItineraryId"),
            rs.getInt("optionId")
    );

    public List<UserItineraryOption> findAll(){ // Gets all options of all user itineraries
        String sql = "SELECT * FROM userItineraryOption";
        return  jdbcTemplate.query(sql, userItineraryOptionRowMapper);
    }

    public List<UserItineraryOption> findOptionsByUserItineraryId(int userItineraryId){ // Gets all options of a user itinerary in particular
        String sql = "SELECT * FROM userItineraryOption WHERE userItineraryId = ?";
        return jdbcTemplate.query(sql, userItineraryOptionRowMapper, userItineraryId);
    }

    public UserItineraryOption findByIds(int userItineraryId, int optionId){
        String sql = "SElECT * FROM userItineraryOption WHERE userItineraryId = ? AND optionId = ?";
        return jdbcTemplate.query(sql, userItineraryOptionRowMapper, userItineraryId, optionId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The option ID : " + optionId + "doesn't exist for user itinerary ID : + " + userItineraryId));
    }

    public UserItineraryOption save (UserItineraryOption userItineraryOption){
        String sql = "INSERT INTO userItineraryOption (userItineraryId, optionId) VALUES (?, ?)";
        jdbcTemplate.update(sql, userItineraryOption.getUserItineraryId(), userItineraryOption.getOptionId());

        return userItineraryOption;
    }


    public UserItineraryOption update(int userItineraryId, int optionId, UserItineraryOption userItineraryOption){
        if(!userItineraryOptionExists(userItineraryId, optionId)){
            throw new RuntimeException("The user itinerary option you are looking for does not exist.");
        }

        String sql = "UPDATE userItineraryOption SET userItineraryId = ?, optionId = ? WHERE userItineraryId = ? AND optionId = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItineraryOption.getUserItineraryId(), userItineraryOption.getOptionId(), userItineraryId, optionId);

        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the user itinerary option with id : " + optionId);
        }

        return this.findByIds(userItineraryId, optionId);
    }

    public boolean delete(int userItineraryId, int optionId){
        String sql = "DELETE FROM userItineraryOption WHERE userItineraryId = ? AND optionId = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItineraryId, optionId);

        return rowsAffected > 0;
    }



    public boolean userItineraryOptionExists(int userItineraryId, int optionId){ // Checks if there are options in a user itinerary
        String sqlCheck = "SELECT COUNT(*) FROM userItineraryOption WHERE userItineraryId = ? AND optionId = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, userItineraryId, optionId);
        return count > 0;
    }
}
