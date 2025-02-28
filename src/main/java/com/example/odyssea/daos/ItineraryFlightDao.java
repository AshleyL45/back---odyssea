package com.example.odyssea.daos;

import com.example.odyssea.entities.itinerary.ItineraryFlight;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItineraryFlightDao {

    private final JdbcTemplate jdbcTemplate;

    public ItineraryFlightDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper <ItineraryFlight> itineraryFlightRowMapper = (rs, rowNum) -> new ItineraryFlight(
            rs.getInt("reservationId"),
            rs.getInt("flightId")
    );



    public List<ItineraryFlight> findAll() {
        String sql = "SELECT * FROM itineraryflight";
        return jdbcTemplate.query(sql, itineraryFlightRowMapper);
    }


    public ItineraryFlight findById(int idReservation) {
        String sql = "SELECT * FROM itineraryflight WHERE reservationId = ?";
        return jdbcTemplate.query(sql, itineraryFlightRowMapper, idReservation)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Le vol de l'itinéraire avec l'ID : " + idReservation + " n'existe pas"));
    }


    public ItineraryFlight save(ItineraryFlight itineraryFlight) {
        String sql = "INSERT INTO itineraryflight (reservationId, flightId) VALUES (?, ?)";
        jdbcTemplate.update(sql, itineraryFlight.getIdReservation(), itineraryFlight.getIdFlight());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        itineraryFlight.setIdReservation(id);
        return itineraryFlight;
    }


}
