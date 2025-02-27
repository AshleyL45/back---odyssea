package com.example.odyssea.daos;

import com.example.odyssea.entities.Activity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ActivityDao {

    private final JdbcTemplate jdbcTemplate;

    public ActivityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 🔹 Sauvegarde une nouvelle activité
     */
    public void save(Activity activity) {
        String sql = """
            INSERT INTO activity (cityId, name, type, physicalEffort, duration, description, price)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql,
                activity.getCityId(),
                activity.getName(),
                activity.getType(),
                activity.getPhysicalEffort(),
                activity.getDuration(),
                activity.getDescription(),
                activity.getPrice()
        );
    }

    /**
     * 🔹 Met à jour une activité existante
     */
    public void update(Activity activity) {
        String sql = """
            UPDATE activity 
            SET cityId = ?, name = ?, type = ?, physicalEffort = ?, duration = ?, description = ?, price = ? 
            WHERE id = ?
        """;
        jdbcTemplate.update(sql,
                activity.getCityId(),
                activity.getName(),
                activity.getType(),
                activity.getPhysicalEffort(),
                activity.getDuration(),
                activity.getDescription(),
                activity.getPrice(),
                activity.getId()
        );
    }

    /**
     * 🔹 Supprime une activité par ID
     */
    public void deleteById(int activityId) {
        String sql = "DELETE FROM activity WHERE id = ?";
        jdbcTemplate.update(sql, activityId);
    }

    /**
     * 🔹 Vérifie si une activité existe via son ID
     */
    public boolean existsById(int activityId) {
        String sql = "SELECT COUNT(*) FROM activity WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{activityId}, Integer.class);
        return count != null && count > 0;
    }

    /**
     * 🔹 Récupère une activité par son ID
     */
    public Optional<Activity> findById(int activityId) {
        String sql = "SELECT * FROM activity WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{activityId}, new ActivityRowMapper())
                .stream()
                .findFirst();
    }

    /**
     * 🔹 Récupère toutes les activités
     */
    public List<Activity> findAll() {
        String sql = "SELECT * FROM activity";
        return jdbcTemplate.query(sql, new ActivityRowMapper());
    }

    /**
     * 🔹 Récupère les activités d'une ville spécifique
     */
    public List<Activity> findActivitiesByCityId(int cityId) {
        String sql = "SELECT * FROM activity WHERE cityId = ?";
        return jdbcTemplate.query(sql, new Object[]{cityId}, new ActivityRowMapper());
    }

    /**
     * 🔹 Récupère les activités d'une ville appartenant à un pays donné.
     */
    public List<Activity> findActivitiesByCityAndCountry(String cityName, String countryName) {
        String sql = """
            SELECT a.*
            FROM activity a
            JOIN city c ON a.cityId = c.id
            JOIN country co ON c.countryId = co.id
            WHERE co.name = ? AND c.name = ?
        """;
        return jdbcTemplate.query(sql, new Object[]{countryName, cityName}, new ActivityRowMapper());
    }

    /**
     * 🔹 Compte le nombre total d'activités
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM activity";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 🔹 RowMapper pour convertir un ResultSet en `Activity`
     */
    private static class ActivityRowMapper implements RowMapper<Activity> {
        @Override
        public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Activity(
                    rs.getInt("id"),
                    rs.getInt("cityId"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getString("physicalEffort"),
                    rs.getInt("duration"),
                    rs.getString("description"),
                    rs.getDouble("price")
            );
        }
    }
}
