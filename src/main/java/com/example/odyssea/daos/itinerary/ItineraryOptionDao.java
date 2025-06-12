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
            rs.getInt("booking"),
            rs.getInt("option_id")
    );


    public List<ItineraryOption> findAll() {
        String sql = "SELECT * FROM itinerary_option";
        return jdbcTemplate.query(sql, itineraryOptionRowMapper);
    }


    public ItineraryOption findById(int idBooking) {
        String sql = "SELECT * FROM itinerary_option WHERE booking = ?";
        return jdbcTemplate.query(sql, itineraryOptionRowMapper, idBooking)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("L'option d'itinéraire avec l'ID : " + idBooking + " n'existe pas"));
    }



    public ItineraryOption save(ItineraryOption itineraryOption) {
        String sql = "INSERT INTO itinerary_option (booking, option_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, itineraryOption.getIdBooking(), itineraryOption.getIdOption());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        itineraryOption.setIdBooking(id);
        return itineraryOption;
    }



    public ItineraryOption update(int id, ItineraryOption itineraryOption) {
        if (!itineraryOptionExists(id)) {
            throw new RuntimeException("Vol d'itinéraire avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE itinerary SET flight_id = ? WHERE booking = ?";
        int rowsAffected = jdbcTemplate.update(sql, itineraryOption.getIdOption(), id);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour du produit avec l'ID : " + id);
        }

        return this.findById(id);
    }



    private boolean itineraryOptionExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM itinerary_option WHERE booking = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }



    public boolean delete(int id) {
        String sql = "DELETE FROM itinerary_option WHERE booking = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }


}
