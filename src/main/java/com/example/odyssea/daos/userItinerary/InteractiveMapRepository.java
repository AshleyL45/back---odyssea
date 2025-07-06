package com.example.odyssea.daos.userItinerary;

import com.example.odyssea.dtos.userItinerary.InteractiveMapDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InteractiveMapRepository {

    private final JdbcTemplate jdbcTemplate;

    public InteractiveMapRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<InteractiveMapDto> getItineraryForUser(int userId, int itineraryId) {
        String sql = "SELECT udp.day_number, c.name AS city_name, c.latitude, c.longitude " +
                "FROM user_daily_plan udp " +
                "INNER JOIN city c ON udp.city_id = c.id " +
                "WHERE udp.user_id = ? AND udp.user_itinerary_id = ? " +
                "ORDER BY udp.day_number ASC";

        return jdbcTemplate.query(sql, new Object[]{userId, itineraryId}, (rs, rowNum) -> {
            InteractiveMapDto dto = new InteractiveMapDto();
            dto.setDayNumber(rs.getInt("day_number"));
            dto.setCityName(rs.getString("city_name"));
            dto.setLatitude(rs.getDouble("latitude"));
            dto.setLongitude(rs.getDouble("longitude"));
            return dto;
        });
    }
}
