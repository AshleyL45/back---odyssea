package com.example.odyssea.daos;

import com.example.odyssea.dtos.Flight.FlightDTO;
import com.example.odyssea.entities.mainTables.Flight;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
public class FlightDao {
    private final JdbcTemplate jdbcTemplate;

    public FlightDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Flight> flightRowMapper = (rs, _) -> new Flight(
            rs.getInt("id"),
            rs.getString("companyName"),
            Duration.ofMillis(rs.getLong("duration")), // A voir
            rs.getDate("departureDate").toLocalDate(),
            rs.getTime("departureTime").toLocalTime(), // A voir
            rs.getString("departureCityIata"),
            rs.getDate("arrivalDate").toLocalDate(),
            rs.getTime("arrivalTime").toLocalTime(), // A voir
            rs.getString("arrivalCityIata"),
            rs.getBigDecimal("price"),
            rs.getString("airplaneName")
    );

    public List<Flight> findAll (){
        String sql = "SELECT * FROM flight";
        return jdbcTemplate.query(sql, flightRowMapper);
    }

    public Flight findById(int flightId){
        String sql = "SELECT * FROM flight WHERE id = ?";
        return jdbcTemplate.query(sql, flightRowMapper, flightId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The flight you are looking for does not exist."));
    }

    public Flight save (Flight flight){
        String sql = "INSERT INTO flight (companyName, duration, departureTime, departureCityIata, arrivalTime, arrivalCityIata, price, airplaneName) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, flight.getCompanyName(), flight.getDuration(), flight.getDepartureTime(), flight.getDepartureCityIata(), flight.getArrivalTime(), flight.getArrivalCityIata(), flight.getPrice(), flight.getAirplaneName());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        flight.setId(id);
        return flight;
    }

    public Flight update(int id, Flight flight){
        if(!flightExists(id)){
            throw new RuntimeException("The flight you are looking for does not exist.");
        }

        String sql = "UPDATE flight SET companyName = ?, duration = ?, departureTime = ?, departureCityIata = ?, arrivalTime = ?, arrivalCityIata = ?, price = ?, airplaneName = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, flight.getCompanyName(), flight.getDuration(), flight.getDepartureTime(), flight.getDepartureCityIata(), flight.getArrivalTime(), flight.getArrivalCityIata(), flight.getPrice(), flight.getAirplaneName(), id);

        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the flight with id : " + id);
        }

        return this.findById(id);

    }

    public boolean delete(int id) {
        String sql = "DELETE FROM flight WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }


    public boolean flightExists(int id){
        String sqlCheck = "SELECT COUNT(*) FROM flight WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, id);
        return count > 0;
    }

    public FlightDTO save(FlightDTO flight) {
        String sql = "INSERT INTO flight (companyName, duration, departureTime, departureCityIata, arrivalTime, arrivalCityIata, price, airplaneName) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, flight.getCompanyName(), flight.getDuration(), flight.getDepartureDateTime(), flight.getDepartureCityIata(), flight.getArrivalDateTime(), flight.getArrivalCityIata(), flight.getPrice(), flight.getAirplaneName());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        flight.setId(id);
        return flight;
    }
}
