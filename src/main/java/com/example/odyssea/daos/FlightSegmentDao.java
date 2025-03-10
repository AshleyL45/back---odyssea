package com.example.odyssea.daos;

import com.example.odyssea.entities.mainTables.FlightSegment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightSegmentDao {

    private final JdbcTemplate jdbcTemplate;

    public FlightSegmentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<FlightSegment> flightSegmentRowMapper = (rs, _) -> new FlightSegment(
            rs.getString("departureAirportIata"),
            rs.getString("arrivalAirportIata"),
            rs.getObject("departureDateTime", LocalDateTime.class),
            rs.getObject("arrivalDateTime", LocalDateTime.class),
            rs.getString("carrierCode"),
            rs.getString("carrierName"),
            rs.getString("aircraftCode"),
            rs.getString("aircraftName"),
            rs.getObject("duration", LocalTime.class)
    );

    public List<FlightSegment> findAll (){
        String sql = "SELECT * FROM flightSegment";
        return jdbcTemplate.query(sql, flightSegmentRowMapper);
    }

    public FlightSegment findById(int id){
        String sql = "SELECT * FROM flightSegment WHERE id = ?";
        return jdbcTemplate.query(sql, flightSegmentRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The flight segment you are looking for does not exist."));
    }


    public FlightSegment update(int id, FlightSegment flightSegment){
        if(!flightSegmentExists(id)){
            throw new RuntimeException("The flight you are looking for does not exist.");
        }

        String sql = "UPDATE planeRide SET departureAirportIata = ?, arrivalAirportIata = ?, departureDateTime = ?, arrivalDateTime = ?, carrierCode = ?, carrierName = ?, aircraftCode = ?, aircraftName = ?, duration = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, flightSegment.getDepartureAirportIata(), flightSegment.getArrivalAirportIata(), flightSegment.getDepartureDateTime(), flightSegment.getArrivalDateTime(), flightSegment.getCarrierCode(), flightSegment.getCarrierName(), flightSegment.getAircraftCode(), flightSegment.getAircraftName(), flightSegment.getDuration(), id);

        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the flight segment with id : " + id);
        }

        return this.findById(id);

    }

    public boolean delete(int id) {
        String sql = "DELETE FROM flightSegment WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }


    public boolean flightSegmentExists(int id){
        String sqlCheck = "SELECT COUNT(*) FROM flightSegment WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, id);
        return count > 0;
    }

    public FlightSegment save(FlightSegment flightSegment) {
        String sql = "INSERT INTO flightSegment (departureAirportIata, arrivalAirportIata, departureDateTime, arrivalDateTime, carrierCode, carrierName, aircraftCode, aircraftName, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Utilisation de Statement.RETURN_GENERATED_KEYS pour récupérer l'ID généré
        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Paramétrage des valeurs dans la requête
            stmt.setString(1, flightSegment.getDepartureAirportIata());
            stmt.setString(2, flightSegment.getArrivalAirportIata());
            stmt.setTimestamp(3, Timestamp.valueOf(flightSegment.getDepartureDateTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(flightSegment.getArrivalDateTime()));
            stmt.setString(5, flightSegment.getCarrierCode());
            stmt.setString(6, flightSegment.getCarrierName());
            stmt.setString(7, flightSegment.getAircraftCode());
            stmt.setString(8, flightSegment.getAircraftName());
            stmt.setString(9, flightSegment.getDuration().toString()); // LocalTime to String

            // Exécution de l'insertion
            int rowsAffected = stmt.executeUpdate();

            // Si l'insertion est réussie, récupérer l'ID généré
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        flightSegment.setId(generatedKeys.getInt(1));  // Récupérer l'ID généré
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du FlightSegment", e);
        }

        return flightSegment;
    }


    public void saveAll(List<FlightSegment> flightSegments){
        String sql = "INSERT INTO flightSegment (departureAirportIata, arrivalAirportIata, departureDateTime, arrivalDateTime, carrierCode, carrierName, aircraftCode, aircraftName, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object []> batchArgs = new ArrayList<>();

        for(FlightSegment flightSegment : flightSegments){
            batchArgs.add(new Object[]{
                    flightSegment.getDepartureAirportIata(),
                    flightSegment.getArrivalAirportIata(),
                    flightSegment.getDepartureDateTime(),
                    flightSegment.getArrivalDateTime(),
                    flightSegment.getCarrierCode(),
                    flightSegment.getCarrierName(),
                    flightSegment.getAircraftCode(),
                    flightSegment.getAircraftName(),
                    flightSegment.getDuration()
            });
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
