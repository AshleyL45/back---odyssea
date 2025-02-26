package com.example.odyssea.daos;

import com.example.odyssea.entities.ItineraryActivityPerDay;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class ItineraryActivityPerDayDao {

    private final JdbcTemplate jdbcTemplate;

    public ItineraryActivityPerDayDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 🔹 Ajouter une activité à un itinéraire pour un jour donné
     */
    public void save(ItineraryActivityPerDay itineraryActivity) {
        String sql = "INSERT INTO itinerary_activities_per_day (itinerary_steps_id, activity_id, day_number) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, itineraryActivity.getItineraryStepsId(), itineraryActivity.getActivityId(), itineraryActivity.getDayNumber());
    }

    /**
     * 🔹 Récupérer toutes les activités d’un itinéraire
     */
    public List<ItineraryActivityPerDay> findByItineraryStepsId(int itineraryStepsId) {
        String sql = "SELECT * FROM itinerary_activities_per_day WHERE itinerary_steps_id = ?";
        return jdbcTemplate.query(sql, new Object[]{itineraryStepsId}, new ItineraryActivityRowMapper());
    }

    /**
     * 🔹 Récupérer les activités d’un itinéraire pour un jour spécifique
     */
    public List<ItineraryActivityPerDay> findByItineraryStepsIdAndDay(int itineraryStepsId, int dayNumber) {
        String sql = "SELECT * FROM itinerary_activities_per_day WHERE itinerary_steps_id = ? AND day_number = ?";
        return jdbcTemplate.query(sql, new Object[]{itineraryStepsId, dayNumber}, new ItineraryActivityRowMapper());
    }

    /**
     * 🔹 Mapper pour convertir un `ResultSet` en objet `ItineraryActivityPerDay`
     */
    private static class ItineraryActivityRowMapper implements RowMapper<ItineraryActivityPerDay> {
        @Override
        public ItineraryActivityPerDay mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ItineraryActivityPerDay(
                    rs.getInt("itinerary_steps_id"),
                    rs.getInt("activity_id"),
                    rs.getInt("day_number")
            );
        }
    }
}
