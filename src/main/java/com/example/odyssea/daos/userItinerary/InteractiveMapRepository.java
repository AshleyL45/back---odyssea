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
        String sql = "SELECT udp.dayNumber, c.name AS cityName, c.latitude, c.longitude " +
                "FROM userDailyPlan udp " +
                "INNER JOIN city c ON udp.cityId = c.id " +
                "WHERE udp.userId = ? AND udp.userItineraryId = ? " +
                "ORDER BY udp.dayNumber ASC";

        return jdbcTemplate.query(sql, new Object[]{userId, itineraryId}, (rs, rowNum) -> {
            InteractiveMapDto dto = new InteractiveMapDto();
            dto.setDayNumber(rs.getInt("dayNumber"));
            dto.setCityName(rs.getString("cityName"));
            dto.setLatitude(rs.getDouble("latitude"));
            dto.setLongitude(rs.getDouble("longitude"));
            return dto;
        });
    }
}
