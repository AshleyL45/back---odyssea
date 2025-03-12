package com.example.odyssea.daos;

import com.example.odyssea.entities.mainTables.FlightSegment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Time;
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
        segment.setDepartureAirportIata(rs.getString("departureAirportIata"));
        segment.setArrivalAirportIata(rs.getString("arrivalAirportIata"));
        segment.setDepartureDateTime(rs.getTimestamp("departureDateTime").toLocalDateTime());
        segment.setArrivalDateTime(rs.getTimestamp("arrivalDateTime").toLocalDateTime());
        segment.setCarrierCode(rs.getString("carrierCode"));
        segment.setCarrierName(rs.getString("carrierName"));
        segment.setAircraftCode(rs.getString("aircraftCode"));
        segment.setAircraftName(rs.getString("aircraftName"));
        Time time = rs.getTime("duration");
        LocalTime localTime = time.toLocalTime();
        segment.setDuration(Duration.ofHours(localTime.getHour())
                .plusMinutes(localTime.getMinute())
                .plusSeconds(localTime.getSecond()));
        return segment;
    };

    public List<FlightSegment> findAll() {
        String sql = "SELECT * FROM flightSegment";
        return jdbcTemplate.query(sql, flightSegmentRowMapper);
    }

    public FlightSegment findById(int id) {
        String sql = "SELECT * FROM flightSegment WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, flightSegmentRowMapper, id);
    }

    public FlightSegment save(FlightSegment segment) {
        String sql = "INSERT INTO flightSegment (departureAirportIata, arrivalAirportIata, departureDateTime, arrivalDateTime, carrierCode, carrierName, aircraftCode, aircraftName, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                segment.getDepartureAirportIata(),
                segment.getArrivalAirportIata(),
                segment.getDepartureDateTime(),
                segment.getArrivalDateTime(),
                segment.getCarrierCode(),
                segment.getCarrierName(),
                segment.getAircraftCode(),
                segment.getAircraftName(),
                convertDurationToSqlTime(segment.getDuration())
        );
        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);
        segment.setId(id);
        return segment;
    }

    public FlightSegment update(int id, FlightSegment segment) {
        String sql = "UPDATE flightSegment SET departureAirportIata = ?, arrivalAirportIata = ?, departureDateTime = ?, arrivalDateTime = ?, carrierCode = ?, carrierName = ?, aircraftCode = ?, aircraftName = ?, duration = ? WHERE id = ?";
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
        String sql = "DELETE FROM flightSegment WHERE id = ?";
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
