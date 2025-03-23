package com.example.odyssea.mapper;

import com.example.odyssea.dtos.ItineraryThemes;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItineraryThemesMapper implements RowMapper<ItineraryThemes> {
    @Override
    public ItineraryThemes mapRow(ResultSet rs, int rowNum) throws SQLException {
        ItineraryThemes itineraryThemes = new ItineraryThemes();
        itineraryThemes.setId(rs.getInt("id"));
        itineraryThemes.setName(rs.getString("name"));
        itineraryThemes.setDescription(rs.getString("description"));
        itineraryThemes.setShortDescription(rs.getString("shortDescription"));
        itineraryThemes.setPrice(rs.getBigDecimal("price"));
        itineraryThemes.setTotalDuration(rs.getInt("totalDuration"));
        itineraryThemes.setThemeId(rs.getInt("themeId"));
        itineraryThemes.setThemeName(rs.getString("themeName"));
        itineraryThemes.setCountriesVisited(rs.getString("countriesVisited"));
        return  itineraryThemes;
    }
}
