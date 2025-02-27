package com.example.odyssea.daos;

import com.example.odyssea.entities.CityDistance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;

@Repository
public class CityDistanceDao {

    private final JdbcTemplate jdbcTemplate;

    public CityDistanceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 🔹 Sauvegarde une nouvelle distance (durée stockée en secondes)
     */
    public void save(CityDistance distance) {
        if (!findByCities(distance.getFromCityId(), distance.getToCityId()).isPresent()) {
            String sql = """
                INSERT INTO cityDistance (fromCityId, toCityId, drivingDurationSeconds, distanceKm) 
                VALUES (?, ?, ?, ?)
            """;
            jdbcTemplate.update(sql, distance.getFromCityId(), distance.getToCityId(), distance.getDrivingDurationSeconds(), distance.getDistanceKm());
        } else {
            updateDistance(distance); // 🔹 Met à jour si la distance existe déjà
        }
    }

    /**
     * 🔹 Met à jour une distance existante (durée en secondes)
     */
    public void updateDistance(CityDistance distance) {
        String sql = """
            UPDATE cityDistance 
            SET drivingDurationSeconds = ?, distanceKm = ? 
            WHERE fromCityId = ? AND toCityId = ?
        """;
        jdbcTemplate.update(sql, distance.getDrivingDurationSeconds(), distance.getDistanceKm(), distance.getFromCityId(), distance.getToCityId());
    }

    /**
     * 🔹 Supprime une distance entre deux villes
     */
    public void deleteDistance(int fromCityId, int toCityId) {
        String sql = "DELETE FROM cityDistance WHERE fromCityId = ? AND toCityId = ?";
        jdbcTemplate.update(sql, fromCityId, toCityId);
    }

    /**
     * 🔹 Supprime une distance par son ID
     */
    public void deleteById(int id) {
        String sql = "DELETE FROM cityDistance WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * 🔹 Vérifie si une distance existe par son ID
     */
    public boolean existsById(int id) {
        String sql = "SELECT COUNT(*) FROM cityDistance WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }

    /**
     * 🔹 Recherche une distance entre deux villes
     */
    public Optional<CityDistance> findByCities(int fromCityId, int toCityId) {
        String sql = "SELECT * FROM cityDistance WHERE fromCityId = ? AND toCityId = ?";
        return jdbcTemplate.query(sql, new Object[]{fromCityId, toCityId}, new DistanceRowMapper())
                .stream()
                .findFirst();
    }

    /**
     * 🔹 Récupère une distance par son ID
     */
    public Optional<CityDistance> findById(int id) {
        String sql = "SELECT * FROM cityDistance WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, new DistanceRowMapper())
                .stream()
                .findFirst();
    }

    /**
     * 🔹 Récupère toutes les distances enregistrées
     */
    public List<CityDistance> findAll() {
        String sql = "SELECT * FROM cityDistance";
        return jdbcTemplate.query(sql, new DistanceRowMapper());
    }

    /**
     * 🔹 RowMapper pour convertir un ResultSet en `CityDistance`
     */
    private static class DistanceRowMapper implements RowMapper<CityDistance> {
        @Override
        public CityDistance mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CityDistance(
                    rs.getInt("id"),
                    rs.getInt("fromCityId"),
                    rs.getInt("toCityId"),
                    rs.getInt("drivingDurationSeconds"), // Stocké en secondes
                    rs.getDouble("distanceKm")
            );
        }
    }
}
