package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.CityDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.odyssea.exceptions.ResourceNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CityDistanceDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CityDistanceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Recherche la distance et la durée entre deux villes spécifiées par leurs identifiants
     */
    public Optional<CityDistance> findByCityIds(int fromCityId, int toCityId) {
        String sql = "SELECT * FROM cityDistance WHERE fromCityId = ? AND toCityId = ?";
        List<CityDistance> distances = jdbcTemplate.query(sql, new CityDistanceRowMapper(), fromCityId, toCityId);
        if (distances.isEmpty()) {
            throw new ResourceNotFoundException("CityDistance not found between city IDs " + fromCityId + " and " + toCityId);
        }
        return Optional.of(distances.get(0));
    }



    /**
     * Enregistre une nouvelle entrée CityDistance en base de données
     */
    public void save(CityDistance cityDistance) {
        String sql = "INSERT INTO cityDistance (fromCityId, toCityId, drivingDurationSeconds, distanceKm) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                cityDistance.getFromCityId(),
                cityDistance.getToCityId(),
                cityDistance.getDrivingDurationSeconds(),
                cityDistance.getDistanceKm()
        );
    }

    /**
     * Met à jour une entrée CityDistance existante
     */
    public void update(CityDistance cityDistance) {
        String sql = "UPDATE cityDistance SET fromCityId = ?, toCityId = ?, drivingDurationSeconds = ?, distanceKm = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                cityDistance.getFromCityId(),
                cityDistance.getToCityId(),
                cityDistance.getDrivingDurationSeconds(),
                cityDistance.getDistanceKm(),
                cityDistance.getId()
        );
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("CityDistance with id " + cityDistance.getId() + " not found for update.");
        }
    }

    /**
     * Supprime une entrée CityDistance par son ID
     */
    public void deleteById(int id) {
        String sql = "DELETE FROM cityDistance WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("CityDistance with id " + id + " not found for deletion.");
        }
    }

    /**
     * Recherche une entrée CityDistance par son ID
     */
    public Optional<CityDistance> findById(int id) {
        String sql = "SELECT * FROM cityDistance WHERE id = ?";
        List<CityDistance> distances = jdbcTemplate.query(sql, new CityDistanceRowMapper(), id);
        if (distances.isEmpty()) {
            throw new ResourceNotFoundException("CityDistance with id " + id + " not found.");
        }
        return Optional.of(distances.get(0));
    }

    /**
     * Récupère toutes les entrées CityDistance de la base de données
     */
    public List<CityDistance> findAll() {
        String sql = "SELECT * FROM cityDistance";
        return jdbcTemplate.query(sql, new CityDistanceRowMapper());
    }

    /**
     * Vérifie si une entrée CityDistance existe en base par son ID
     */
    public boolean existsById(int id) {
        String sql = "SELECT COUNT(*) FROM cityDistance WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    /**
     * RowMapper interne pour transformer un ResultSet en objet CityDistance
     */
    private static class CityDistanceRowMapper implements RowMapper<CityDistance> {
        @Override
        public CityDistance mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CityDistance(
                    rs.getInt("id"),
                    rs.getInt("fromCityId"),
                    rs.getInt("toCityId"),
                    rs.getInt("drivingDurationSeconds"),
                    rs.getDouble("distanceKm")
            );
        }
    }
}
