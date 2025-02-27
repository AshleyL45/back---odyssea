package com.example.odyssea.daos;

import com.example.odyssea.entities.Itinerary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ItineraryDao {

    private final JdbcTemplate jdbcTemplate;

    public ItineraryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    
    private final RowMapper<Itinerary> itineraryRowMapper =(rs, _) -> new Itinerary(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("shortDescription"),
            rs.getInt("stock"),
            rs.getBigDecimal("price"),
            rs.getTime("totalDuration")
    );




}
