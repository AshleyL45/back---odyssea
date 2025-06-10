package com.example.odyssea.mapper;

import com.example.odyssea.dtos.mainTables.ItinerarySummary;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItineraryThemesMapper implements RowMapper<ItinerarySummary> {
    @Override
    public ItinerarySummary mapRow(ResultSet rs, int rowNum) throws SQLException {
        ItinerarySummary itinerarySummary = new ItinerarySummary();
        itinerarySummary.setId(rs.getInt("id"));
        itinerarySummary.setName(rs.getString("name"));
        itinerarySummary.setDescription(rs.getString("description"));
        itinerarySummary.setShortDescription(rs.getString("short_description"));
        itinerarySummary.setPrice(rs.getBigDecimal("price"));
        itinerarySummary.setTotalDuration(rs.getInt("total_duration"));
        itinerarySummary.setThemeId(rs.getInt("theme_id"));
        itinerarySummary.setThemeName(rs.getString("theme_name"));
        itinerarySummary.setCountriesVisited(rs.getString("countries_visited"));
        return itinerarySummary;
    }
}
