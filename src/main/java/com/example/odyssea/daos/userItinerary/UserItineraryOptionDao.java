package com.example.odyssea.daos.userItinerary;

import com.example.odyssea.entities.Option;
import com.example.odyssea.entities.userItinerary.UserItineraryOption;
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

    public List<UserItineraryOption> findOptionsByUserItineraryId(int userItineraryId){
        String sql = "SELECT * FROM options IN (SELECT * FROM userItineraryOption WHERE userItineraryId = ?)";
        return jdbcTemplate.query(sql, userItineraryOptionRowMapper, userItineraryId);
    }
}
