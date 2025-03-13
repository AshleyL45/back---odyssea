package com.example.odyssea.daos;

import com.example.odyssea.entities.mainTables.FlightSegmentRide;
import com.example.odyssea.entities.mainTables.PlaneRide;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightSegmentRideDao {
    private final JdbcTemplate jdbcTemplate;

    public FlightSegmentRideDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<FlightSegmentRide> flightSegmentRideRowMapper = (rs, _) -> new FlightSegmentRide(
            rs.getInt("planeRideId"),
            rs.getInt("flightSegmentId")
    );

    public List<FlightSegmentRide> findAll (){
        String sql = "SELECT * FROM flightSegmentRide";
        return jdbcTemplate.query(sql, flightSegmentRideRowMapper);
    }

    public FlightSegmentRide findById(int planeRideId, int flightSegmentId){
        String sql = "SELECT * FROM flightSegmentRide WHERE planeRideId = ? AND flightSegmentId = ?";
        return jdbcTemplate.query(sql, flightSegmentRideRowMapper, planeRideId, flightSegmentId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The flight segment for plane ride id " + planeRideId + " you are looking for does not exist."));
    }


    public FlightSegmentRide update(int planeRideId, int flightSegmentId, FlightSegmentRide flightSegmentRide){
        if(!flightSegmentExists(planeRideId, flightSegmentId)){
            throw new RuntimeException("The flight segment for plane ride id " + planeRideId + "doesn't exist.");
        }

        String sql = "UPDATE flightSegmentRide SET planeRideId = ?, flightSegmentId = ? WHERE planeRideId = ? AND flightSegmentId = ?";
        int rowsAffected = jdbcTemplate.update(sql, flightSegmentRide.getPlaneRideId(), flightSegmentRide.getFlightSegmentId(), planeRideId, flightSegmentId);
        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the flight segment with id : " + flightSegmentId + " from offer " + planeRideId);
        }

        return this.findById(planeRideId, flightSegmentId);

    }

    public boolean delete(int planeRideId, int flightSegmentId) {
        String sql = "DELETE FROM flightSegmentRide WHERE planeRideId = ? AND flightSegmentId = ?";
        int rowsAffected = jdbcTemplate.update(sql, planeRideId, flightSegmentId);
        return rowsAffected > 0;
    }


    public boolean flightSegmentExists(int planeRideId, int flightSegmentId){
        String sqlCheck = "SELECT COUNT(*) FROM flightSegmentRide WHERE planeRideId = ? AND flightSegmentId = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, planeRideId, flightSegmentId);
        return count > 0;
    }

    public FlightSegmentRide save(FlightSegmentRide flightSegmentRide) {
        String sql = "INSERT INTO flightSegmentRide (planeRideId, flightSegmentId) VALUES (?, ?)";
        jdbcTemplate.update(sql, flightSegmentRide.getPlaneRideId(), flightSegmentRide.getFlightSegmentId());

        return flightSegmentRide;
    }

    public void saveAll(List<FlightSegmentRide> flightSegmentRides){
        String sql = "INSERT INTO flightSegmentRide (planeRideId, flightSegmentId) VALUES (?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (FlightSegmentRide flightSegmentRide : flightSegmentRides) {
            batchArgs.add(new Object[]{
                    flightSegmentRide.getFlightSegmentId(),
                    flightSegmentRide.getPlaneRideId()
            });
        }
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
