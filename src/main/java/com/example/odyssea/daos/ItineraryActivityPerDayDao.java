package com.example.odyssea.daos;

import com.example.odyssea.entities.itinerary.ItineraryActivityPerDay;
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

    public ItineraryActivityPerDayDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Récupérer une activité par son ID
    public Optional<ItineraryActivityPerDay> findById(int itineraryStepsId, int activityId, int dayNumber) {
        String sql = "SELECT * FROM activitiesPerDay WHERE itineraryStepsId = ? AND activityId = ? AND dayNumber = ?";
        return jdbcTemplate.query(sql, new Object[]{itineraryStepsId, activityId, dayNumber}, new ItineraryActivityRowMapper())
                .stream().findFirst();
    }

    // Récupérer toutes les activités d’un itinéraire
    public List<ItineraryActivityPerDay> findAll() {
        String sql = "SELECT * FROM activitiesPerDay";
        return jdbcTemplate.query(sql, new ItineraryActivityRowMapper());
    }

    // Sauvegarder une activité (ajout)
    public void save(ItineraryActivityPerDay itineraryActivity) {
        String sql = "INSERT INTO activitiesPerDay (itineraryStepsId, activityId, dayNumber) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, itineraryActivity.getItineraryStepsId(), itineraryActivity.getActivityId(), itineraryActivity.getDayNumber());
    }

    // Mettre à jour une activité existante
    public void update(ItineraryActivityPerDay itineraryActivity) {
        String sql = "UPDATE activitiesPerDay SET dayNumber = ? WHERE itineraryStepsId = ? AND activityId = ?";
        jdbcTemplate.update(sql, itineraryActivity.getDayNumber(), itineraryActivity.getItineraryStepsId(), itineraryActivity.getActivityId());
    }

    // Supprimer une activité par son ID
    public void deleteById(int itineraryStepsId, int activityId, int dayNumber) {
        String sql = "DELETE FROM activitiesPerDay WHERE itineraryStepsId = ? AND activityId = ? AND dayNumber = ?";
        jdbcTemplate.update(sql, itineraryStepsId, activityId, dayNumber);
    }

    // Mapper pour convertir un ResultSet en objet ItineraryActivityPerDay
    private static class ItineraryActivityRowMapper implements RowMapper<ItineraryActivityPerDay> {
        @Override
        public ItineraryActivityPerDay mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ItineraryActivityPerDay(
                    rs.getInt("itineraryStepsId"),
                    rs.getInt("activityId"),
                    rs.getInt("dayNumber")
            );
        }
    }
}


