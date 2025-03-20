package com.example.odyssea.daos.flight;

import com.example.odyssea.entities.mainTables.PlaneRide;
import com.example.odyssea.dtos.Flight.PlaneRideDTO;
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
import java.time.LocalDateTime;
import java.util.List;

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
     * Retrouver un planeRIde par ses segments
     */

    public List<PlaneRide> findBySegmentDetails(
            String departureIata,
            String arrivalIata,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime
    ) {
        String sql = "SELECT pr.* FROM planeRide pr " +
                "JOIN flightSegmentRide fsr ON pr.id = fsr.planeRideId " +
                "JOIN flightSegment fs ON fsr.flightSegmentId = fs.id " +
                "WHERE fs.departureAirportIata = ? " +
                "AND fs.arrivalAirportIata = ? " +
                "AND fs.departureDateTime = ? " +
                "AND fs.arrivalDateTime = ?";

        return jdbcTemplate.query(sql, new Object[]{departureIata, arrivalIata, departureTime, arrivalTime}, planeRideRowMapper);
    }
}
