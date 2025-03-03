package com.example.odyssea.daos;

import com.example.odyssea.entities.itinerary.ItineraryActivityPerDay;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ItineraryActivityPerDayDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ItineraryActivityPerDayDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Enregistre une nouvelle association activité/jour dans un itinéraire
     */
    public void save(ItineraryActivityPerDay entity) {
        String sql = "INSERT INTO activitiesPerDay (itineraryStepId, activityId, dayNumber) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, entity.getItineraryStepId(), entity.getActivityId(), entity.getDayNumber());
    }

    /**
     * Supprime une association activité/jour par sa clé composite
     */
    public void delete(int itineraryStepId, int activityId, int dayNumber) {
        String sql = "DELETE FROM activitiesPerDay WHERE itineraryStepId = ? AND activityId = ? AND dayNumber = ?";
        int rowsAffected = jdbcTemplate.update(sql, itineraryStepId, activityId, dayNumber);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("No activity found for itineraryStepId " + itineraryStepId
                    + ", activityId " + activityId + ", dayNumber " + dayNumber);
        }
    }

    /**
     * Recherche une association activité/jour par sa clé composite
     */
    public Optional<ItineraryActivityPerDay> findByCompositeKey(int itineraryStepId, int activityId, int dayNumber) {
        String sql = "SELECT * FROM activitiesPerDay WHERE itineraryStepId = ? AND activityId = ? AND dayNumber = ?";
        List<ItineraryActivityPerDay> list = jdbcTemplate.query(sql, new ItineraryActivityPerDayRowMapper(),
                itineraryStepId, activityId, dayNumber);
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No activity found for itineraryStepId " + itineraryStepId
                    + ", activityId " + activityId + ", dayNumber " + dayNumber);
        }
        return Optional.of(list.get(0));
    }

    /**
     * Récupère toutes les associations activité/jour pour tous les itinéraires
     */
    public List<ItineraryActivityPerDay> findAll() {
        String sql = "SELECT * FROM activitiesPerDay";
        return jdbcTemplate.query(sql, new ItineraryActivityPerDayRowMapper());
    }

    /**
     * RowMapper pour transformer un ResultSet en objet ItineraryActivityPerDay
     */
    private static class ItineraryActivityPerDayRowMapper implements RowMapper<ItineraryActivityPerDay> {
        @Override
        public ItineraryActivityPerDay mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ItineraryActivityPerDay(
                    rs.getInt("itineraryStepId"),
                    rs.getInt("activityId"),
                    rs.getInt("dayNumber")
            );
        }
    }
}
