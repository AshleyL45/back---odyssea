package com.example.odyssea.daos.flight;

import com.example.odyssea.dtos.Flight.FlightDTO;
import com.example.odyssea.entities.mainTables.Flight;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class FlightDao {
    private final JdbcTemplate jdbcTemplate;

    public FlightDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper qui reconstruit un Flight en séparant la date et l'heure à partir du TIMESTAMP
    private final RowMapper<Flight> flightRowMapper = (rs, rowNum) -> {
        int id = rs.getInt("id");
        String companyName = rs.getString("companyName");

        // Conversion de la colonne duration (type TIME) en Duration
        Time durationTime = rs.getTime("duration");
        LocalTime lt = durationTime.toLocalTime();
        Duration duration = Duration.ofHours(lt.getHour())
                .plusMinutes(lt.getMinute())
                .plusSeconds(lt.getSecond());

        // Récupération du TIMESTAMP pour le départ, puis séparation en LocalDate et LocalTime
        LocalDateTime dtDeparture = rs.getTimestamp("departureDate").toLocalDateTime();
        LocalDate departureDate = dtDeparture.toLocalDate();
        LocalTime departureTime = dtDeparture.toLocalTime();

        // Idem pour l'arrivée
        LocalDateTime dtArrival = rs.getTimestamp("arrivalDate").toLocalDateTime();
        LocalDate arrivalDate = dtArrival.toLocalDate();
        LocalTime arrivalTime = dtArrival.toLocalTime();

        String departureCityIata = rs.getString("departureCityIata");
        String arrivalCityIata = rs.getString("arrivalCityIata");
        BigDecimal price = rs.getBigDecimal("price");
        String airplaneName = rs.getString("airplaneName");

        return new Flight(
                id,
                companyName,
                duration,
                departureDate,
                departureTime,
                departureCityIata,
                arrivalDate,
                arrivalTime,
                arrivalCityIata,
                price,
                airplaneName
        );
    };

    public List<Flight> findAll() {
        String sql = "SELECT * FROM flight";
        return jdbcTemplate.query(sql, flightRowMapper);
    }

    public Flight findById(int flightId) {
        String sql = "SELECT * FROM flight WHERE id = ?";
        return jdbcTemplate.query(sql, flightRowMapper, flightId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The flight you are looking for does not exist."));
    }

    public Flight save(Flight flight) {
        String sql = "INSERT INTO flight (companyName, duration, departureDate, departureCityIata, arrivalDate, arrivalCityIata, price, airplaneName) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Convertir Duration en Time pour la colonne duration
        Duration d = flight.getDuration();
        LocalTime durationLT = LocalTime.of((int)d.toHours(), (int)(d.toMinutes() % 60), (int)(d.getSeconds() % 60));
        Time durationTime = Time.valueOf(durationLT);

        // Recomposer le TIMESTAMP à partir de la date et l'heure pour le départ et l'arrivée
        LocalDateTime departureDateTime = LocalDateTime.of(flight.getDepartureDate(), flight.getDepartureTime());
        LocalDateTime arrivalDateTime = LocalDateTime.of(flight.getArrivalDate(), flight.getArrivalTime());

        jdbcTemplate.update(sql,
                flight.getCompanyName(),
                durationTime,
                departureDateTime,
                flight.getDepartureCityIata(),
                arrivalDateTime,
                flight.getArrivalCityIata(),
                flight.getPrice(),
                flight.getAirplaneName()
        );

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);
        flight.setId(id);
        return flight;
    }

    public Flight update(int id, Flight flight) {
        if (!flightExists(id)) {
            throw new RuntimeException("The flight you are looking for does not exist.");
        }

        String sql = "UPDATE flight SET companyName = ?, duration = ?, departureDate = ?, departureCityIata = ?, arrivalDate = ?, arrivalCityIata = ?, price = ?, airplaneName = ? WHERE id = ?";
        Duration d = flight.getDuration();
        LocalTime durationLT = LocalTime.of((int)d.toHours(), (int)(d.toMinutes() % 60), (int)(d.getSeconds() % 60));
        Time durationTime = Time.valueOf(durationLT);
        LocalDateTime departureDateTime = LocalDateTime.of(flight.getDepartureDate(), flight.getDepartureTime());
        LocalDateTime arrivalDateTime = LocalDateTime.of(flight.getArrivalDate(), flight.getArrivalTime());

        int rowsAffected = jdbcTemplate.update(sql,
                flight.getCompanyName(),
                durationTime,
                departureDateTime,
                flight.getDepartureCityIata(),
                arrivalDateTime,
                flight.getArrivalCityIata(),
                flight.getPrice(),
                flight.getAirplaneName(),
                id
        );

        if (rowsAffected <= 0) {
            throw new RuntimeException("Failed to update the flight with id : " + id);
        }

        return this.findById(id);
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM flight WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    public boolean flightExists(int id) {
        String sqlCheck = "SELECT COUNT(*) FROM flight WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, id);
        return count > 0;
    }

    public FlightDTO save(FlightDTO flight) {
        String sql = "INSERT INTO flight (companyName, duration, departureDate, departureCityIata, arrivalDate, arrivalCityIata, price, airplaneName) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Duration d = flight.getDuration();
        LocalTime durationLT = LocalTime.of((int)d.toHours(), (int)(d.toMinutes() % 60), (int)(d.getSeconds() % 60));
        Time durationTime = Time.valueOf(durationLT);
        jdbcTemplate.update(sql,
                flight.getCompanyName(),
                durationTime,
                flight.getDepartureDateTime(),
                flight.getDepartureCityIata(),
                flight.getArrivalDateTime(),
                flight.getArrivalCityIata(),
                flight.getPrice(),
                flight.getAirplaneName()
        );

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);
        flight.setId(id);
        return flight;
    }
}
