package com.example.odyssea.daos.itinerary;

import com.example.odyssea.dtos.mainTables.ItineraryThemes;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import com.example.odyssea.mapper.ItineraryThemesMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
            rs.getString("shortDescription"),
            rs.getInt("stock"),
            rs.getBigDecimal("price"),
            rs.getInt("totalDuration"),
            rs.getInt("themeId")
    );


    public List<Itinerary> findAll() {
        String sql = "SELECT * FROM itinerary";
        return jdbcTemplate.query(sql, itineraryRowMapper);
    }

    public List<ItineraryThemes> findAllItinerariesWithTheme(){
        String sql = "SELECT \n" +
                "    itinerary.*, \n" +
                "    theme.name AS themeName,\n" +
                "    GROUP_CONCAT(DISTINCT country.name ORDER BY country.name ASC SEPARATOR ', ') AS countriesVisited\n" +
                "FROM \n" +
                "    dailyItinerary\n" +
                "INNER JOIN \n" +
                "    country ON country.id = dailyItinerary.countryId\n" +
                "INNER JOIN \n" +
                "    itinerary ON dailyItinerary.itineraryId = itinerary.id\n" +
                "INNER JOIN \n" +
                "    theme ON itinerary.themeId = theme.id\n" +
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

}
