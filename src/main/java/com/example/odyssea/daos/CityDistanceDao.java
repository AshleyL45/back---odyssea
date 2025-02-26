package com.example.odyssea.daos;

import com.example.odyssea.entities.CityDistance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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
            String sql = "INSERT INTO cityDistance (fromCityId, toCityId, drivingDurationSeconds, distanceKm) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, distance.getFromCityId(), distance.getToCityId(), distance.getDrivingDurationSeconds(), distance.getDistanceKm());
        } else {
            updateDistance(distance); // 🔹 Met à jour si la distance existe déjà
        }
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
     * 🔹 Met à jour une distance existante (durée en secondes)
     */
    public void updateDistance(CityDistance distance) {
        String sql = "UPDATE cityDistance SET drivingDurationSeconds = ?, distanceKm = ? WHERE fromCityId = ? AND toCityId = ?";
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
     * 🔹 Récupère la durée formatée "X heures Y minutes" entre deux villes
     */
    public String getFormattedDurationBetweenCities(int fromCityId, int toCityId) {
        Optional<CityDistance> distance = findByCities(fromCityId, toCityId);
        return distance.map(CityDistance::getFormattedDuration)
                .orElse("Durée non disponible");
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
