package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.exceptions.ActivityNotFound;
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

    public boolean cityExists(int cityId) {
        String sql = "SELECT COUNT(*) FROM city WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cityId);
        return count != null && count > 0;
    }

    public boolean existsById(int id) {
        String sql = "SELECT COUNT(*) FROM activity WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public void save(Activity activity) {
        String sql = "INSERT INTO activity (city_id, name, type, physical_effort, duration, description, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
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

    public void update(Activity activity) {
        String sql = "UPDATE activity SET city_id = ?, name = ?, type = ?, physical_effort = ?, duration = ?, description = ?, price = ? WHERE id = ?";
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
            throw new ActivityNotFound("Activity not found with id: " + activity.getId());
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM activity WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new ActivityNotFound("Activity not found with id: " + id);
        }
    }

    public Optional<Activity> findById(int id) {
        String sql = "SELECT * FROM activity WHERE id = ?";
        List<Activity> activities = jdbcTemplate.query(sql, new ActivityRowMapper(), id);
        if (activities == null || activities.isEmpty()) {
            throw new ActivityNotFound("Activity not found with id: " + id);
        }
        return Optional.of(activities.get(0));
    }

    public List<Activity> findAll() {
        String sql = "SELECT * FROM activity";
        return jdbcTemplate.query(sql, new ActivityRowMapper());
    }

    public List<Activity> findTop5ByCityId(int cityId) {
        String sql = "SELECT * FROM activity WHERE city_id = ? LIMIT 5";
        return jdbcTemplate.query(sql, new ActivityRowMapper(), cityId);
    }

    public boolean activityExists(int cityId, String name) {
        String sql = "SELECT COUNT(*) FROM activity WHERE city_id = ? AND name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cityId, name);
        return count != null && count > 0;
    }

    private static class ActivityRowMapper implements RowMapper<Activity> {
        @Override
        public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
            int id = rs.getInt("id");
            int cityId = rs.getInt("city_id");
            String name = rs.getString("name");
            String type = rs.getString("type");
            String physicalEffort = rs.getString("physical_effort");
            LocalTime duration = rs.getTime("duration").toLocalTime();
            String description = rs.getString("description");
            double price = rs.getDouble("price");
            return new Activity(id, cityId, name, type, physicalEffort, duration, description, price);
        }
    }
}
