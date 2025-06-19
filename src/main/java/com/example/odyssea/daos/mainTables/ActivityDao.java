package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.exceptions.ActivityNotFound;
import com.example.odyssea.exceptions.ActivityAlreadyExistsException;
import com.example.odyssea.exceptions.DatabaseException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

@Repository
public class ActivityDao {

    private final JdbcTemplate jdbcTemplate;

    public ActivityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Sauvegarde une nouvelle activité, mais vérifie d'abord l'absence de doublon
    public Activity save(Activity activity) {
        String checkSql = "SELECT COUNT(*) FROM activity WHERE city_id = ? AND name = ?";
        Integer count = jdbcTemplate.queryForObject(
                checkSql,
                Integer.class,
                activity.getCityId(),
                activity.getName()
        );

        if (count != null && count > 0) {
            throw new ActivityAlreadyExistsException(
                    "Activity already exists for cityId=" + activity.getCityId() +
                            " and name=\"" + activity.getName() + "\""
            );
        }

        String insertSql = "INSERT INTO activity "
                + "(city_id, name, type, physical_effort, duration, description, price) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        int rows = jdbcTemplate.update(
                insertSql,
                activity.getCityId(),
                activity.getName(),
                activity.getType(),
                activity.getPhysicalEffort(),
                activity.getDuration(),
                activity.getDescription(),
                activity.getPrice()
        );

        if (rows != 1) {
            throw new DatabaseException("Failed to insert activity for cityId=" + activity.getCityId());
        }

        return activity;
    }

    // Met à jour une activité existante, ou lève ActivityNotFound si l'ID est introuvable
    public Activity update(Activity activity) {
        String sql = "UPDATE activity SET "
                + "city_id = ?, name = ?, type = ?, physical_effort = ?, "
                + "duration = ?, description = ?, price = ? "
                + "WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(
                sql,
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
            throw new ActivityNotFound("Activity not found with id=" + activity.getId());
        }

        return activity;
    }

    // Supprime une activité ou lève ActivityNotFound si l'ID est introuvable
    public void deleteById(int id) {
        String sql = "DELETE FROM activity WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new ActivityNotFound("Activity not found with id=" + id);
        }
    }

    // Recherche une activité par ID, ou lève ActivityNotFound
    public Activity findById(int id) {
        String sql = "SELECT * FROM activity WHERE id = ?";
        List<Activity> list = jdbcTemplate.query(sql, new ActivityRowMapper(), id);
        if (list.isEmpty()) {
            throw new ActivityNotFound("Activity not found with id=" + id);
        }
        return list.get(0);
    }

    // Pour importer ou lister, on veut ici un simple retour de liste vide ou non
    public List<Activity> findTop5ByCityId(int cityId) {
        String sql = "SELECT * FROM activity WHERE city_id = ? LIMIT 5";
        return jdbcTemplate.query(sql, new ActivityRowMapper(), cityId);
    }

    private static class ActivityRowMapper implements RowMapper<Activity> {
        @Override
        public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Activity(
                    rs.getInt("id"),
                    rs.getInt("city_id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getString("physical_effort"),
                    rs.getTime("duration").toLocalTime(),
                    rs.getString("description"),
                    rs.getDouble("price")
            );
        }
    }
}
