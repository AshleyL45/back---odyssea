package com.example.odyssea.daos.itinerary;

import com.example.odyssea.entities.itinerary.ItineraryOption;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItineraryOptionDao {


    private final JdbcTemplate jdbcTemplate;

    public ItineraryOptionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ItineraryOption> itineraryOptionRowMapper = (rs, rowNum) -> new ItineraryOption(
            rs.getInt("reservationId"),
            rs.getInt("optionId")
    );


    public List<ItineraryOption> findAll() {
        String sql = "SELECT * FROM itineraryoption";
        return jdbcTemplate.query(sql, itineraryOptionRowMapper);
    }


    public ItineraryOption findById(int idReservation) {
        String sql = "SELECT * FROM itineraryoption WHERE reservationId = ?";
        return jdbcTemplate.query(sql, itineraryOptionRowMapper, idReservation)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("L'option d'itinéraire avec l'ID : " + idReservation + " n'existe pas"));
    }



    public ItineraryOption save(ItineraryOption itineraryOption) {
        String sql = "INSERT INTO itineraryoption (reservationId, optionId) VALUES (?, ?)";
        jdbcTemplate.update(sql, itineraryOption.getIdReservation(), itineraryOption.getIdOption());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        itineraryOption.setIdReservation(id);
        return itineraryOption;
    }



    public ItineraryOption update(int id, ItineraryOption itineraryOption) {
        if (!itineraryOptionExists(id)) {
            throw new RuntimeException("Vol d'itinéraire avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE itinérary SET flightId = ? WHERE reservationId = ?";
        int rowsAffected = jdbcTemplate.update(sql, itineraryOption.getIdOption(), id);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour du produit avec l'ID : " + id);
        }

        return this.findById(id);
    }



    private boolean itineraryOptionExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM itineraryoption WHERE reservationId = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }



    public boolean delete(int id) {
        String sql = "DELETE FROM itineraryoption WHERE reservationId = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }


}
