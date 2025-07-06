package com.example.odyssea.daos.userItinerary;

import com.example.odyssea.dtos.userItinerary.OptionWithItineraryId;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.entities.userItinerary.UserItineraryOption;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import com.example.odyssea.exceptions.UserItineraryDatabaseException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserItineraryOptionDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public UserItineraryOptionDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<UserItineraryOption> userItineraryOptionRowMapper = (rs, _) -> new UserItineraryOption(
            rs.getInt("user_itinerary_id"),
            rs.getInt("option_id")
    );

    public List<UserItineraryOption> findAll(){ // Gets all options of all user itineraries
        String sql = "SELECT * FROM user_itinerary_option";
        return  jdbcTemplate.query(sql, userItineraryOptionRowMapper);
    }

    public List<Option> findOptionsByUserItineraryId(int userItineraryId){ // Gets all options of a user itinerary in particular
        String sql = "SELECT options.* FROM user_itinerary_option INNER JOIN options ON user_itinerary_option.option_id = options.id WHERE user_itinerary_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userItineraryId}, new BeanPropertyRowMapper<>(Option.class));
    }

    public UserItineraryOption findByIds(int userItineraryId, int optionId){
        String sql = "SElECT * FROM user_itinerary_option WHERE user_itinerary_id = ? AND option_id = ?";
        return jdbcTemplate.query(sql, userItineraryOptionRowMapper, userItineraryId, optionId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The option ID : " + optionId + "doesn't exist for user itinerary ID : + " + userItineraryId));
    }

    public List<OptionWithItineraryId> findAllOptionsByUserId(List<Integer> ids){
        String sql = "SELECT uio.user_itinerary_id, o.* FROM options o JOIN user_itinerary_option uio ON o.id = uio.option_id WHERE uio.user_itinerary_id IN (:ids)";
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        return namedParameterJdbcTemplate.query(sql, params, OptionWithItineraryId.rowMapper());
    }

    public UserItineraryOption save(int userItineraryId, int optionId) {
        try {
            String sql = "INSERT INTO user_itinerary_option (user_itinerary_id, option_id) VALUES (?, ?)";
            jdbcTemplate.update(sql, userItineraryId, optionId);

            return new UserItineraryOption(userItineraryId, optionId);
        } catch (Exception e) {
            throw new UserItineraryDatabaseException(e.getMessage());
        }
    }



    public UserItineraryOption update(int userItineraryId, int optionId, UserItineraryOption userItineraryOption){
        if(!userItineraryOptionExists(userItineraryId, optionId)){
            throw new RuntimeException("The user itinerary option you are looking for does not exist.");
        }

        String sql = "UPDATE user_itinerary_option SET user_itinerary_id = ?, option_id = ? WHERE user_itinerary_id = ? AND option_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItineraryOption.getUserItineraryId(), userItineraryOption.getOptionId(), userItineraryId, optionId);

        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the user itinerary option with id : " + optionId);
        }

        return this.findByIds(userItineraryId, optionId);
    }

    public boolean delete(int userItineraryId, int optionId){
        String sql = "DELETE FROM user_itinerary_option WHERE user_itinerary_id = ? AND option_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItineraryId, optionId);

        return rowsAffected > 0;
    }



    public boolean userItineraryOptionExists(int userItineraryId, int optionId){ // Checks if there are options in a user itinerary
        String sqlCheck = "SELECT COUNT(*) FROM user_itinerary_option WHERE user_itinerary_id = ? AND option_id = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, userItineraryId, optionId);
        return count > 0;
    }
}
