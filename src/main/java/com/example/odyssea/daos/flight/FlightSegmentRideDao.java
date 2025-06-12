package com.example.odyssea.daos.flight;

import com.example.odyssea.entities.mainTables.FlightSegment;
import com.example.odyssea.entities.mainTables.FlightSegmentRide;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
        ride.setPlaneRideId(rs.getInt("plane_ride_id"));
        ride.setFlightSegmentId(rs.getInt("flight_segment_id"));
        return ride;
    };

    public List<FlightSegmentRide> findAll() {
        String sql = "SELECT * FROM flight_segment_ride";
        return jdbcTemplate.query(sql, flightSegmentRideRowMapper);
    }

    public FlightSegmentRide findByIds(int planeRideId, int flightSegmentId) {
        String sql = "SELECT * FROM flight_segment_ride WHERE plane_ride_id = ? AND flight_segment_id = ?";
        return jdbcTemplate.queryForObject(sql, flightSegmentRideRowMapper, planeRideId, flightSegmentId);
    }

    public List<FlightSegment> findSegmentsByPlaneId(int planeRideId) {
        String sql = "SELECT flight_segment.* from flight_segment_ride\n" +
                "INNER JOIN flight_segment ON flight_segment_ride.flight_segment_id = flight_segment.id\n" +
                "WHERE flight_segment_ride.plane_ride_id = 2";
        return jdbcTemplate.query(sql, new Object[]{planeRideId}, new BeanPropertyRowMapper<>(FlightSegment.class));
    }

    public FlightSegmentRide save(FlightSegmentRide ride) {
        String sql = "INSERT INTO flight_segment_ride (plane_ride_id, flight_segment_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, ride.getPlaneRideId(), ride.getFlightSegmentId());
        return ride;
    }

    public boolean delete(int planeRideId, int flightSegmentId) {
        String sql = "DELETE FROM flight_segment_ride WHERE plane_ride_id = ? AND flight_segment_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, planeRideId, flightSegmentId);
        return rowsAffected > 0;
    }
}
