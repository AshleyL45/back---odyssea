package com.example.odyssea.daos.flight;

import com.example.odyssea.dtos.flight.*;
import com.example.odyssea.entities.mainTables.PlaneRide;
import com.example.odyssea.exceptions.DatabaseException;
import com.example.odyssea.exceptions.FlightSegmentsNotFound;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PlaneRideDao {

    private final JdbcTemplate jdbcTemplate;

    public PlaneRideDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PlaneRide> planeRideRowMapper = (rs, rowNum) -> {
        int id = rs.getInt("id");
        boolean oneWay = rs.getBoolean("one_way");
        BigDecimal totalPrice = rs.getBigDecimal("total_price");
        String currency = rs.getString("currency");
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime createdAt = (ts != null) ? ts.toLocalDateTime() : null;
        return new PlaneRide(id, oneWay, totalPrice, currency, createdAt);
    };

    /**
     * Retourne tous les vols enregistrés.
     */
    public List<PlaneRide> findAll() {
        String sql = "SELECT * FROM plane_ride";
        return jdbcTemplate.query(sql, planeRideRowMapper);
    }

    /**
     * Retourne le vol correspondant à l'id, ou lève une exception si introuvable.
     */
    public PlaneRide findById(int id) {
        String sql = "SELECT * FROM plane_ride WHERE id = ?";
        return jdbcTemplate.query(sql, planeRideRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("This flight doesn't exist."));
    }

    /**
     * Enregistre un nouveau vol et retourne l'entité avec son id généré.
     */
    public PlaneRide save(PlaneRide planeRide) {
        String sql = "INSERT INTO plane_ride (one_way, total_price, currency) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setBoolean(1, planeRide.isOneWay());
            ps.setBigDecimal(2, planeRide.getTotalPrice());
            ps.setString(3, planeRide.getCurrency());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) {
            planeRide.setId(key.intValue());
        } else {
            planeRide.setId(0);
        }
        return planeRide;
    }

    /**
     * Met à jour un vol existant. Si le vol n'existe pas ou si la mise à jour échoue, lève une ResourceNotFoundException.
     */
    public PlaneRide update(int id, PlaneRide planeRide) {
        if (!flightExists(id)) {
            throw new ResourceNotFoundException("This flight doesn't exist.");
        }
        String sql = "UPDATE plane_ride SET one_way = ?, total_price = ?, currency = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                planeRide.isOneWay(),
                planeRide.getTotalPrice(),
                planeRide.getCurrency(),
                id);
        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Flight update with id " + id + " failed.");
        }
        return this.findById(id);
    }

    /**
     * Supprime un vol par son identifiant.
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM plane_ride WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    /**
     * Vérifie si un vol existe dans la base.
     */
    public boolean flightExists(int id) {
        String sqlCheck = "SELECT COUNT(*) FROM plane_ride WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, id);
        return count > 0;
    }

    /**
     * Méthode de sauvegarde pour PlaneRideDTO si nécessaire.
     */
    public PlaneRideDTO save(PlaneRideDTO flight) {
        String sql = "INSERT INTO plane_ride (one_way, total_price, currency) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setBoolean(1, flight.isOneWay());
            ps.setBigDecimal(2, flight.getTotalPrice());
            ps.setString(3, flight.getCurrency());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) {
            flight.setId(key.intValue());
        } else {
            flight.setId(0);
        }
        return flight;
    }

    /**
     * Retourne un vol avec ses segments
     */
    public FlightItineraryDTO getPlaneRideById(Integer rideId) {
        String sql = "SELECT \n" +
                "    fs.id AS segment_id,\n" +
                "    fs.departure_airport_iata, fs.departure_date_time,\n" +
                "    fs.arrival_airport_iata, fs.arrival_date_time,\n" +
                "    fs.carrier_code, fs.carrier_name,\n" +
                "    fs.aircraft_code, fs.aircraft_name,\n" +
                "    fs.duration,\n" +
                "    pr.id AS plane_ride_id\n" +
                "FROM \n" +
                "    flight_segment_ride fsr\n" +
                "INNER JOIN \n" +
                "    flight_segment fs ON fsr.flight_segment_id = fs.id\n" +
                "INNER JOIN \n" +
                "    plane_ride pr ON fsr.plane_ride_id = pr.id\n" +
                "WHERE \n" +
                "    pr.id = ?;";

        if(rideId == null || rideId == 0 ){
            return null;
        }

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, rideId);

        if (rows.isEmpty()) throw new FlightSegmentsNotFound("Flight segments for plane ride ID : " + rideId + " haven't been found.");

        List<FlightSegmentDTO> segments = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            AirportDTO departure = new AirportDTO(
                    (String) row.get("departure_airport_iata"),
                    ((LocalDateTime) row.get("departure_date_time"))
            );

            AirportDTO arrival = new AirportDTO(
                    (String) row.get("arrival_airport_iata"),
                    ((LocalDateTime) row.get("arrival_date_time"))
            );

            AircraftDTO aircraft = new AircraftDTO(
                    (String) row.get("aircraft_code")
            );

            FlightSegmentDTO segment = new FlightSegmentDTO();
            segment.setId(String.valueOf(row.get("segment_id")));
            segment.setDeparture(departure);
            segment.setArrival(arrival);
            segment.setCarrierCode((String) row.get("carrier_code"));
            segment.setCarrierName((String) row.get("carrier_name"));
            segment.setAircraftCode(aircraft);
            segment.setAircraftName((String) row.get("aircraft_name"));
            segment.setDuration(row.get("duration").toString());

            segments.add(segment);
        }

        Map<String, Object> firstRow = rows.getFirst();
        Integer planeRideId = (Integer) firstRow.get("plane_ride_id");
        Duration totalDuration = calculateTotalDuration(segments);

        return new FlightItineraryDTO(planeRideId, segments, totalDuration);
    }


    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM plane_ride WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    private Duration calculateTotalDuration(List<FlightSegmentDTO> segments) {
        if (segments.isEmpty()) return Duration.ZERO;

        LocalDateTime start = segments.getFirst().getDeparture().getDateTime();
        LocalDateTime end = segments.getLast().getArrival().getDateTime();

        return Duration.between(start, end);
    }
}
