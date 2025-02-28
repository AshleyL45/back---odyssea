package com.example.odyssea.daos;

import com.example.odyssea.entities.itinerary.Itinerary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;

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


    public List<Itinerary> findAll() {
        String sql = "SELECT * FROM itinerary";
        return jdbcTemplate.query(sql, itineraryRowMapper);
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
                .orElseThrow(() -> new RuntimeException("Itinéraire avec l'Id : " + idItinerary + " n'existe pas"));
    }



    public Itinerary findByName(String name) {
        String sql = "SELECT * FROM itinerary WHERE name = ?";
        return jdbcTemplate.query(sql, itineraryRowMapper, name)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Itinéraire avec le nom : " + name + " n'existe pas"));
    }



    public Itinerary findByDuration(Time totalDuration) {
        String sql = "SELECT * FROM itinerary WHERE totalDuration = ?";
        return jdbcTemplate.query(sql, itineraryRowMapper, totalDuration)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Itinéraire avec une durée de : " + totalDuration + " n'existe pas"));
    }




    public Itinerary save(Itinerary itinerary) {
        String sql = "INSERT INTO itinerary (name, description, shortDescription, stock, price, totalDuration) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, itinerary.getItineraryName(), itinerary.getDescription(), itinerary.getShortDescription(), itinerary.getStock(), itinerary.getPrice(), itinerary.getTotalDuration());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        itinerary.setIdItinerary(id);
        return itinerary;
    }


    public Itinerary update(int id, Itinerary itinerary) {
        if (!itineraryExists(id)) {
            throw new RuntimeException("Itinéraire avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE itinerary SET name = ?, description = ?, shortDescription = ?, stock = ?, price = ?, totalDuration = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, itinerary.getItineraryName(), itinerary.getDescription(), itinerary.getShortDescription(), itinerary.getStock(), itinerary.getPrice(), itinerary.getTotalDuration(), id);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour du produit avec l'ID : " + id);
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
