package com.example.odyssea.daos;

import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class HotelDao {

    private final JdbcTemplate jdbcTemplate;

    public HotelDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Vérifie que le cityId existe dans la table city
     */
    public boolean cityExists(int cityId) {
        String sql = "SELECT COUNT(*) FROM city WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cityId);
        return count != null && count > 0;
    }

    /**
     * Vérifie si un hôtel existe dans la table hotel par son id
     */
    public boolean existsById(int id) {
        String sql = "SELECT COUNT(*) FROM hotel WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    /**
     * Enregistre un nouvel hôtel
     */
    public void save(Hotel hotel) {
        // Vérifier que la ville existe avant d'enregistrer l'hôtel
        if (!cityExists(hotel.getCityId())) {
            throw new ResourceNotFoundException("City with id " + hotel.getCityId() + " not found.");
        }
        String sql = "INSERT INTO hotel (cityId, name, starRating, description, price) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                hotel.getCityId(),
                hotel.getName(),
                hotel.getStarRating(),
                hotel.getDescription(),
                hotel.getPrice()
        );
    }

    /**
     * Met à jour un hôtel existant
     */
    public void update(Hotel hotel) {
        // Vérifier que la ville existe avant de mettre à jour l'hôtel
        if (!cityExists(hotel.getCityId())) {
            throw new ResourceNotFoundException("City with id " + hotel.getCityId() + " not found.");
        }
        String sql = "UPDATE hotel SET cityId = ?, name = ?, starRating = ?, description = ?, price = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                hotel.getCityId(),
                hotel.getName(),
                hotel.getStarRating(),
                hotel.getDescription(),
                hotel.getPrice(),
                hotel.getId()
        );
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Hotel with id " + hotel.getId() + " not found for update.");
        }
    }

    /**
     * Supprime un hôtel par son identifiant
     */
    public void deleteById(int id) {
        String sql = "DELETE FROM hotel WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Hotel with id " + id + " not found for deletion.");
        }
    }

    /**
     * Recherche un hôtel par son identifiant
     * Retourne Optional.empty() si non trouvé
     */
    public Optional<Hotel> findById(int id) {
        String sql = "SELECT * FROM hotel WHERE id = ?";
        List<Hotel> hotels = jdbcTemplate.query(sql, new HotelRowMapper(), id);
        if (hotels.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(hotels.get(0));
    }

    /**
     * Récupère tous les hôtels
     */
    public List<Hotel> findAll() {
        String sql = "SELECT * FROM hotel";
        List<Hotel> hotels = jdbcTemplate.query(sql, new HotelRowMapper());
        if (hotels.isEmpty()) {
            throw new ResourceNotFoundException("No hotels found.");
        }
        return hotels;
    }

    /**
     * Récupère les hôtels d'une ville donnée
     */
    public List<Hotel> findByCityId(int cityId) {
        String sql = "SELECT * FROM hotel WHERE cityId = ?";
        List<Hotel> hotels = jdbcTemplate.query(sql, new HotelRowMapper(), cityId);
        if (hotels.isEmpty()) {
            throw new ResourceNotFoundException("No hotels found for city id " + cityId);
        }
        return hotels;
    }

    /**
     * Récupère les hôtels d'une ville donnée ayant un certain standing (starRating)
     */
    public List<Hotel> findByCityIdAndStarRating(int cityId, int starRating) {
        String sql = "SELECT * FROM hotel WHERE cityId = ? AND starRating = ?";
        List<Hotel> hotels = jdbcTemplate.query(sql, new HotelRowMapper(), cityId, starRating);
        if (hotels.isEmpty()) {
            throw new ResourceNotFoundException("No hotels found with star rating " + starRating + " in city id " + cityId);
        }
        return hotels;
    }

    /**
     * RowMapper pour transformer un ResultSet en objet Hotel
     */
    private static class HotelRowMapper implements RowMapper<Hotel> {
        @Override
        public Hotel mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Hotel(
                    rs.getInt("id"),
                    rs.getInt("cityId"),
                    rs.getString("name"),
                    rs.getInt("starRating"),
                    rs.getString("description"),
                    rs.getDouble("price")
            );
        }
    }
}
