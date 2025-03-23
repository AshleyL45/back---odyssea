package com.example.odyssea.daos;

import com.example.odyssea.dtos.ItineraryThemes;
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



    public Itinerary findByName(String name) {
        String sql = "SELECT * FROM itinerary WHERE name = ?";
        return jdbcTemplate.query(sql, itineraryRowMapper, name)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Itinéraire avec le nom : " + name + " n'existe pas"));
    }



    public Itinerary findByDuration(int totalDuration) {
        String sql = "SELECT * FROM itinerary WHERE totalDuration = ?";
        return jdbcTemplate.query(sql, itineraryRowMapper, totalDuration)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Itinéraire avec une durée de : " + totalDuration + " n'existe pas"));
    }




    public Itinerary save(Itinerary itinerary) {
        String sql = "INSERT INTO itinerary (name, description, shortDescription, stock, price, totalDuration, themeId) VALUES (?, ?, ?, ?, ?, ?,?)";
        jdbcTemplate.update(sql, itinerary.getName(), itinerary.getDescription(), itinerary.getShortDescription(), itinerary.getStock(), itinerary.getPrice(), itinerary.getTotalDuration(), itinerary.getThemeId());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        itinerary.setId(id);
        return itinerary;
    }


    public Itinerary update(int id, Itinerary itinerary) {
        if (!itineraryExists(id)) {
            throw new RuntimeException("Itinéraire avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE itinerary SET name = ?, description = ?, shortDescription = ?, stock = ?, price = ?, totalDuration = ?, themeId = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, itinerary.getName(), itinerary.getDescription(), itinerary.getShortDescription(), itinerary.getStock(), itinerary.getPrice(), itinerary.getTotalDuration(), itinerary.getThemeId(), id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Cannot update itinerary with ID : " + id);
        }

        return this.findById(id);
    }



    private boolean itineraryExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM itinerary WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }



    public boolean delete(int id) {
        String sql = "DELETE FROM itinerary WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

}
