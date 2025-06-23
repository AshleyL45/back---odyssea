package com.example.odyssea.daos.itinerary;

import com.example.odyssea.dtos.mainTables.ItinerarySummary;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import com.example.odyssea.mapper.ItineraryThemesMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ItineraryDao {

    private final JdbcTemplate jdbcTemplate;

    public ItineraryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public final RowMapper<Itinerary> itineraryRowMapper =(rs, _) -> new Itinerary(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("short_description"),
            rs.getInt("stock"),
            rs.getBigDecimal("price"),
            rs.getInt("total_duration"),
            rs.getInt("theme_id")
    );


    public List<Itinerary> findAll() {
        String sql = "SELECT * FROM itinerary";
        return jdbcTemplate.query(sql, itineraryRowMapper);
    }

    public List<ItinerarySummary> findAllItinerariesSummaries(){
        String sql = "SELECT \n" +
                "    itinerary.*, \n" +
                "    theme.name AS theme_name,\n" +
                "    GROUP_CONCAT(DISTINCT country.name ORDER BY country.name ASC SEPARATOR ', ') AS countries_visited\n" +
                "FROM \n" +
                "    daily_itinerary\n" +
                "INNER JOIN \n" +
                "    country ON country.id = daily_itinerary.country_id\n" +
                "INNER JOIN \n" +
                "    itinerary ON daily_itinerary.itinerary_id = itinerary.id\n" +
                "INNER JOIN \n" +
                "    theme ON itinerary.theme_id = theme.id\n" +
                "GROUP BY \n" +
                "    itinerary.id;";
        return jdbcTemplate.query(sql, new ItineraryThemesMapper());
    }

    public List<Itinerary> searchItinerary(String query){
        String sql = "SELECT * FROM itinerary WHERE LOWER(name) LIKE LOWER(?) ";
        return jdbcTemplate.query(sql,itineraryRowMapper, "%" + query + "%");
    }


    public Itinerary findById(int idItinerary) {
        String sql = "SELECT * FROM itinerary WHERE id = ?";
        return jdbcTemplate.query(sql, itineraryRowMapper, idItinerary)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Itinéraire avec l'Id : " + idItinerary + " n'existe pas"));
    }

    public List<Itinerary> findValidItineraries(List<String> excludedCountries) {
        if (excludedCountries == null || excludedCountries.isEmpty()) {
            return findAll();
        }
        String inClause = excludedCountries.stream()
                .map(c -> "'" + c.replace("'", "''") + "'")
                .collect(Collectors.joining(", "));
        String sql = ""
                + "SELECT * FROM itinerary "
                + "WHERE id NOT IN ( "
                + "  SELECT DISTINCT dt.itinerary_id "
                + "  FROM daily_itinerary dt "
                + "  JOIN country c ON dt.country_id = c.id "
                + "  WHERE c.name IN (" + inClause + ")"
                + ")";
        return jdbcTemplate.query(sql, itineraryRowMapper);
    }

}