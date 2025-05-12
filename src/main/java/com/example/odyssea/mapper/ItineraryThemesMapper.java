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
        itinerarySummary.setShortDescription(rs.getString("shortDescription"));
        itinerarySummary.setPrice(rs.getBigDecimal("price"));
        itinerarySummary.setTotalDuration(rs.getInt("totalDuration"));
        itinerarySummary.setThemeId(rs.getInt("themeId"));
        itinerarySummary.setThemeName(rs.getString("themeName"));
        itinerarySummary.setCountriesVisited(rs.getString("countriesVisited"));
        return itinerarySummary;
    }
}
