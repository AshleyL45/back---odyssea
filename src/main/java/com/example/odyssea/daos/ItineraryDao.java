package com.example.odyssea.daos;

import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.exceptions.ResourceNotFoundException;
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

    public final RowMapper<Itinerary> itineraryRowMapper = (rs, _) -> new Itinerary(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("shortDescription"),
            rs.getInt("stock"),
            rs.getBigDecimal("price"),
            rs.getInt("totalDuration"),
            rs.getInt("themeId")
    );

    /**
     * Récupère tous les itinéraires
     */
    public List<Itinerary> findAll() {
        String sql = "SELECT * FROM itinerary";
        return jdbcTemplate.query(sql, itineraryRowMapper);
    }

    /**
     * Recherche un itinéraire dont le nom contient la chaîne spécifiée
     */
    public List<Itinerary> searchItinerary(String query) {
        String sql = "SELECT * FROM itinerary WHERE LOWER(name) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, itineraryRowMapper, "%" + query + "%");
    }

    /**
     * Recherche un itinéraire par son ID
     */
    public Itinerary findById(int idItinerary) {
        String sql = "SELECT * FROM itinerary WHERE id = ?";
        return jdbcTemplate.query(sql, itineraryRowMapper, idItinerary)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary with ID " + idItinerary + " not found"));
    }

    /**
     * Recherche un itinéraire par son nom exact
     */
    public Itinerary findByName(String name) {
        String sql = "SELECT * FROM itinerary WHERE name = ?";
        return jdbcTemplate.query(sql, itineraryRowMapper, name)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary with name '" + name + "' not found"));
    }

    /**
     * Recherche un itinéraire par sa durée totale
     */
    public Itinerary findByDuration(int totalDuration) {
        String sql = "SELECT * FROM itinerary WHERE totalDuration = ?";
        return jdbcTemplate.query(sql, itineraryRowMapper, totalDuration)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary with duration " + totalDuration + " not found"));
    }

    /**
     * Enregistre un nouvel itinéraire et renvoie l'objet avec l'ID généré
     */
    public Itinerary save(Itinerary itinerary) {
        String sql = "INSERT INTO itinerary (name, description, shortDescription, stock, price, totalDuration, themeId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, itinerary.getName(), itinerary.getDescription(), itinerary.getShortDescription(), itinerary.getStock(), itinerary.getPrice(), itinerary.getTotalDuration(), itinerary.getThemeId());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        itinerary.setId(id);
        return itinerary;
    }

    /**
     * Met à jour un itinéraire existant identifié par son ID
     */
    public Itinerary update(int id, Itinerary itinerary) {
        if (!itineraryExists(id)) {
            throw new ResourceNotFoundException("Itinerary with ID " + id + " not found");
        }

        String sql = "UPDATE itinerary SET name = ?, description = ?, shortDescription = ?, stock = ?, price = ?, totalDuration = ?, themeId = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, itinerary.getName(), itinerary.getDescription(), itinerary.getShortDescription(), itinerary.getStock(), itinerary.getPrice(), itinerary.getTotalDuration(), itinerary.getThemeId(), id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Failed to update itinerary with ID " + id);
        }

        return this.findById(id);
    }

    private boolean itineraryExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM itinerary WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }

    /**
     * Supprime un itinéraire par son ID
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM itinerary WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Itinerary with ID " + id + " not found for deletion");
        }
        return rowsAffected > 0;
    }
}
