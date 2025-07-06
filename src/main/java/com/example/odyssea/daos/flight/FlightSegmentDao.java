package com.example.odyssea.daos.flight;

import com.example.odyssea.entities.mainTables.FlightSegment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Repository
public class FlightSegmentDao {

    private final JdbcTemplate jdbcTemplate;

    public FlightSegmentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<FlightSegment> flightSegmentRowMapper = (rs, rowNum) -> {
        FlightSegment segment = new FlightSegment();
        segment.setId(rs.getInt("id"));
        segment.setDepartureAirportIata(rs.getString("departure_airport_iata"));
        segment.setArrivalAirportIata(rs.getString("arrival_airport_iata"));
        segment.setDepartureDateTime(rs.getTimestamp("departure_date_time").toLocalDateTime());
        segment.setArrivalDateTime(rs.getTimestamp("arrival_date_time").toLocalDateTime());
        segment.setCarrierCode(rs.getString("carrier_code"));
        segment.setCarrierName(rs.getString("carrier_name"));
        segment.setAircraftCode(rs.getString("aircraft_code"));
        segment.setAircraftName(rs.getString("aircraft_name"));
        Time time = rs.getTime("duration");
        LocalTime localTime = time.toLocalTime();
        segment.setDuration(Duration.ofHours(localTime.getHour())
                .plusMinutes(localTime.getMinute())
                .plusSeconds(localTime.getSecond()));
        return segment;
    };

    public List<FlightSegment> findAll() {
        String sql = "SELECT * FROM flight_segment";
        return jdbcTemplate.query(sql, flightSegmentRowMapper);
    }

    public FlightSegment findById(int id) {
        String sql = "SELECT * FROM flight_segment WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, flightSegmentRowMapper, id);
    }

    public FlightSegment save(FlightSegment segment) {
        String sql = "INSERT INTO flight_segment (departure_airport_iata, arrival_airport_iata, departure_date_time, arrival_date_time, carrier_code, carrier_name, aircraft_code, aircraft_name, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, segment.getDepartureAirportIata());
            ps.setString(2, segment.getArrivalAirportIata());
            ps.setTimestamp(3, Timestamp.valueOf(segment.getDepartureDateTime()));
            ps.setTimestamp(4, Timestamp.valueOf(segment.getArrivalDateTime()));
            ps.setString(5, segment.getCarrierCode());
            ps.setString(6, segment.getCarrierName());
            ps.setString(7, segment.getAircraftCode());
            ps.setString(8, segment.getAircraftName());
            ps.setTime(9, convertDurationToSqlTime(segment.getDuration()));
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) {
            segment.setId(key.intValue());
        }
        return segment;
    }

    public FlightSegment update(int id, FlightSegment segment) {
        String sql = "UPDATE flight_segment SET departure_airport_iata = ?, arrival_airport_iata = ?, departure_date_time = ?, arrival_date_time = ?, carrier_code = ?, carrier_name = ?, aircraft_code = ?, aircraft_name = ?, duration = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                segment.getDepartureAirportIata(),
                segment.getArrivalAirportIata(),
                segment.getDepartureDateTime(),
                segment.getArrivalDateTime(),
                segment.getCarrierCode(),
                segment.getCarrierName(),
                segment.getAircraftCode(),
                segment.getAircraftName(),
                convertDurationToSqlTime(segment.getDuration()),
                id
        );
        return findById(id);
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM flight_segment WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    private Time convertDurationToSqlTime(Duration duration) {
        long seconds = duration.getSeconds();
        int hours = (int) (seconds / 3600);
        int minutes = (int) ((seconds % 3600) / 60);
        int secs = (int) (seconds % 60);
        return Time.valueOf(LocalTime.of(hours, minutes, secs));
    }



}
