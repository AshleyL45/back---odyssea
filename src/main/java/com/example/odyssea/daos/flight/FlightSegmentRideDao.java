package com.example.odyssea.daos.flight;

import com.example.odyssea.entities.mainTables.FlightSegmentRide;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlightSegmentRideDao {

    private final JdbcTemplate jdbcTemplate;

    public FlightSegmentRideDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<FlightSegmentRide> flightSegmentRideRowMapper = (rs, rowNum) -> {
        FlightSegmentRide ride = new FlightSegmentRide();
        ride.setPlaneRideId(rs.getInt("planeRideId"));
        ride.setFlightSegmentId(rs.getInt("flightSegmentId"));
        return ride;
    };

    public List<FlightSegmentRide> findAll() {
        String sql = "SELECT * FROM flightSegmentRide";
        return jdbcTemplate.query(sql, flightSegmentRideRowMapper);
    }

    public FlightSegmentRide findByIds(int planeRideId, int flightSegmentId) {
        String sql = "SELECT * FROM flightSegmentRide WHERE planeRideId = ? AND flightSegmentId = ?";
        return jdbcTemplate.queryForObject(sql, flightSegmentRideRowMapper, planeRideId, flightSegmentId);
    }

    public FlightSegmentRide save(FlightSegmentRide ride) {
        String sql = "INSERT INTO flightSegmentRide (planeRideId, flightSegmentId) VALUES (?, ?)";
        jdbcTemplate.update(sql, ride.getPlaneRideId(), ride.getFlightSegmentId());
        return ride;
    }

    public boolean delete(int planeRideId, int flightSegmentId) {
        String sql = "DELETE FROM flightSegmentRide WHERE planeRideId = ? AND flightSegmentId = ?";
        int rowsAffected = jdbcTemplate.update(sql, planeRideId, flightSegmentId);
        return rowsAffected > 0;
    }
}
