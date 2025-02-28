package com.example.odyssea.daos;


import com.example.odyssea.entities.itinerary.ItineraryStep;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItineraryStepDao {


    private final JdbcTemplate jdbcTemplate;

    public ItineraryStepDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ItineraryStep> itineraryStepRowMapper = (rs, rowNum) -> new ItineraryStep(
            rs.getInt("id"),
            rs.getInt("itineraryId"),
            rs.getInt("cityId"),
            rs.getInt("countryId"),
            rs.getInt("hotelId"),
            rs.getInt("position"),
            rs.getInt("dayNumber"),
            rs.getString("descriptionPerDay")
    );



    public List<ItineraryStep> findAll() {
        String sql = "SELECT * FROM itinerarystep";
        return jdbcTemplate.query(sql, itineraryStepRowMapper);
    }



    public ItineraryStep findById(int idItineraryStep) {
        String sql = "SELECT * FROM itinerarystep WHERE id = ?";
        return jdbcTemplate.query(sql, itineraryStepRowMapper, idItineraryStep)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Itinéraire avec l'ID : " + idItineraryStep + " n'existe pas"));
    }


    public ItineraryStep save(ItineraryStep itineraryStep) {
        String sql = "INSERT INTO itinerarystep (itineraryId, cityId, countryId, hotelId, position, dayNumber, descriptionPerDay) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, itineraryStep.getPosition(), itineraryStep.getDayNumber(), itineraryStep.getDescriptionPerDay());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        itineraryStep.setIdItineraryStep(id);
        return itineraryStep;
    }



    public ItineraryStep update(int id, ItineraryStep itineraryStep) {
        if (!itineraryStepExists(id)) {
            throw new RuntimeException("Etape de l'itinéraire avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE itinerarystep SET itineraryId = ?, cityId =?, countryId = ?, hotelId = ?, position = ?, dayNumber = ?, descriptionPerDay = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, itineraryStep.getPosition(), itineraryStep.getDayNumber(), itineraryStep.getDescriptionPerDay(), id);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour du produit avec l'ID : " + id);
        }

        return this.findById(id);
    }


    private boolean itineraryStepExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM itinerarystep WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }



    public boolean delete(int id) {
        String sql = "DELETE FROM itinerarystep WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }


}
