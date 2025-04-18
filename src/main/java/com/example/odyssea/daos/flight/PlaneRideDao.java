package com.example.odyssea.daos.flight;

import com.example.odyssea.dtos.flight.*;
import com.example.odyssea.entities.mainTables.PlaneRide;
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
        BigDecimal totalPrice = rs.getBigDecimal("totalPrice");
        String currency = rs.getString("currency");
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime createdAt = (ts != null) ? ts.toLocalDateTime() : null;
        return new PlaneRide(id, oneWay, totalPrice, currency, createdAt);
    };

    /**
     * Retourne tous les vols enregistrés.
     */
    public List<PlaneRide> findAll() {
        String sql = "SELECT * FROM planeRide";
        return jdbcTemplate.query(sql, planeRideRowMapper);
    }

    /**
     * Retourne le vol correspondant à l'id, ou lève une exception si introuvable.
     */
    public PlaneRide findById(int id) {
        String sql = "SELECT * FROM planeRide WHERE id = ?";
        return jdbcTemplate.query(sql, planeRideRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("This flight doesn't exist."));
    }

    /**
     * Enregistre un nouveau vol et retourne l'entité avec son id généré.
     */
    public PlaneRide save(PlaneRide planeRide) {
        String sql = "INSERT INTO planeRide (one_way, totalPrice, currency) VALUES (?, ?, ?)";
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
        String sql = "UPDATE planeRide SET one_way = ?, totalPrice = ?, currency = ? WHERE id = ?";
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
        String sql = "DELETE FROM planeRide WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    /**
     * Vérifie si un vol existe dans la base.
     */
    public boolean flightExists(int id) {
        String sqlCheck = "SELECT COUNT(*) FROM planeRide WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, id);
        return count > 0;
    }

    /**
     * Méthode de sauvegarde pour PlaneRideDTO si nécessaire.
     */
    public PlaneRideDTO save(PlaneRideDTO flight) {
        String sql = "INSERT INTO planeRide (one_way, totalPrice, currency) VALUES (?, ?, ?)";
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
    public FlightItineraryDTO getPlaneRideById(int rideId) {
        String sql = "SELECT \n" +
                "    fs.id AS segmentId,\n" +
                "    fs.departureAirportIata, fs.departureDateTime,\n" +
                "    fs.arrivalAirportIata, fs.arrivalDateTime,\n" +
                "    fs.carrierCode, fs.carrierName,\n" +
                "    fs.aircraftCode, fs.aircraftName,\n" +
                "    fs.duration,\n" +
                "    pr.id AS planeRideId\n" +
                "FROM \n" +
                "    flightSegmentRide fsr\n" +
                "INNER JOIN \n" +
                "    flightSegment fs ON fsr.flightSegmentId = fs.id\n" +
                "INNER JOIN \n" +
                "    planeRide pr ON fsr.planeRideId = pr.id\n" +
                "WHERE \n" +
                "    pr.id = ?;";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, rideId);

        if (rows.isEmpty()) throw new FlightSegmentsNotFound("Flight segments for plane ride ID : " + rideId + " haven't been found.");

        List<FlightSegmentDTO> segments = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            AirportDTO departure = new AirportDTO(
                    (String) row.get("departureAirportIata"),
                    ((LocalDateTime) row.get("departureDateTime"))
            );

            AirportDTO arrival = new AirportDTO(
                    (String) row.get("arrivalAirportIata"),
                    ((LocalDateTime) row.get("arrivalDateTime"))
            );

            AircraftDTO aircraft = new AircraftDTO(
                    (String) row.get("aircraftCode")
            );

            FlightSegmentDTO segment = new FlightSegmentDTO();
            segment.setId(String.valueOf(row.get("segmentId")));
            segment.setDeparture(departure);
            segment.setArrival(arrival);
            segment.setCarrierCode((String) row.get("carrierCode"));
            segment.setCarrierName((String) row.get("carrierName"));
            segment.setAircraftCode(aircraft);
            segment.setAircraftName((String) row.get("aircraftName"));
            segment.setDuration(row.get("duration").toString());

            segments.add(segment);
        }

        Map<String, Object> firstRow = rows.getFirst();
        Integer planeRideId = (Integer) firstRow.get("planeRideId");
        Duration totalDuration = calculateTotalDuration(segments);

        return new FlightItineraryDTO(planeRideId, segments, totalDuration);
    }


    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM planeRide WHERE id = ?";
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
