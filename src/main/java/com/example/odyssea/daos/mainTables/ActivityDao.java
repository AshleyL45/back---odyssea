package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ActivityDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ActivityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Vérifie si la ville existe dans la table city
     */
    public boolean cityExists(int cityId) {
        String sql = "SELECT COUNT(*) FROM city WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cityId);
        return count != null && count > 0;
    }

    /**
     * Vérifie si une activité existe dans la table activity
     */
    public boolean existsById(int id) {
        String sql = "SELECT COUNT(*) FROM activity WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    /**
     * Enregistre une nouvelle activité
     */
    public void save(Activity activity) {
        String sql = "INSERT INTO activity (cityId, name, type, physicalEffort, duration, description, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
     * Met à jour une activité existante
     */
    public void update(Activity activity) {
        String sql = "UPDATE activity SET cityId = ?, name = ?, type = ?, physicalEffort = ?, duration = ?, description = ?, price = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                activity.getCityId(),
                activity.getName(),
                activity.getType(),
                activity.getPhysicalEffort(),
                activity.getDuration(),
                activity.getDescription(),
                activity.getPrice(),
                activity.getId()
        );
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Activity not found with id: " + activity.getId());
        }
    }

    /**
     * Supprime une activité par son identifiant
     */
    public void deleteById(int id) {
        String sql = "DELETE FROM activity WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Activity not found with id: " + id);
        }
    }

    /**
     * Récupère une activité par son identifiant
     */
    public Optional<Activity> findById(int id) {
        String sql = "SELECT * FROM activity WHERE id = ?";
        List<Activity> activities = jdbcTemplate.query(sql, new ActivityRowMapper(), id);
        if (activities.isEmpty()) {
            throw new ResourceNotFoundException("Activity not found with id: " + id);
        }
        return Optional.of(activities.get(0));
    }

    /**
     * Récupère toutes les activités
     */
    public List<Activity> findAll() {
        String sql = "SELECT * FROM activity";
        return jdbcTemplate.query(sql, new ActivityRowMapper());
    }

    /**
     * Récupère les 5 premières activités pour une ville donnée
     */
    public List<Activity> findTop5ByCityId(int cityId) {
        String sql = "SELECT * FROM activity WHERE cityId = ? LIMIT 5";
        List<Activity> activities = jdbcTemplate.query(sql, new ActivityRowMapper(), cityId);
        // Si aucune activité n'est trouvée, jdbcTemplate.query renvoie déjà une liste vide,
        // donc il n'est pas nécessaire de lancer une exception ici.
        return activities;
    }


    /**
     * RowMapper pour transformer un ResultSet en objet Activity
     */
    private static class ActivityRowMapper implements RowMapper<Activity> {
        @Override
        public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
            int id = rs.getInt("id");
            int cityId = rs.getInt("cityId");
            String name = rs.getString("name");
            String type = rs.getString("type");
            String physicalEffort = rs.getString("physicalEffort");
            // Conversion du TIME en minutes
            //java.sql.Time time = rs.getTime("duration");
            LocalTime duration = rs.getTime("duration").toLocalTime();
            /*if (time != null) {
                duration = time.toLocalTime().getHour() * 60 + time.toLocalTime().getMinute();
            }*/
            String description = rs.getString("description");
            double price = rs.getDouble("price");
            return new Activity(id, cityId, name, type, physicalEffort, duration, description, price);
        }
    }

}
