package com.example.odyssea.daos;

import com.example.odyssea.entities.mainTables.Flight;
import com.example.odyssea.entities.mainTables.FlightSegment;
import com.example.odyssea.entities.mainTables.PlaneRide;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PlaneRideDao {
    private final JdbcTemplate jdbcTemplate;


    public PlaneRideDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PlaneRide> planeRideRowMapper = (rs, _) -> new PlaneRide(
            rs.getInt("id"),
            rs.getBoolean("one_way"),
            rs.getBigDecimal("totalPrice"),
            rs.getString("currency"),
            rs.getObject("created_at", LocalDateTime.class)
    );

    public List<PlaneRide> findAll (){
        String sql = "SELECT * FROM planeRide";
        return jdbcTemplate.query(sql, planeRideRowMapper);
    }

    public PlaneRide findById(int id){
        String sql = "SELECT * FROM planeRide WHERE id = ?";
        return jdbcTemplate.query(sql, planeRideRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The plane ride you are looking for does not exist."));
    }

    public List<FlightSegment> findFlightSegmentsOfAPlaneRide(int planeRideId){
        String sql = "SELECT flightSegment.* FROM flightSegmentRide\n" +
                "INNER JOIN flightSegment ON flightSegmentRide.flightSegmentId = flightSegment.id\n" +
                "INNER JOIN planeRide ON flightSegmentRide.planeRideId = planeRide.id WHERE flightSegmentRide.planeRideId = ? \n";
        return jdbcTemplate.query(sql, new Object[] {planeRideId}, new BeanPropertyRowMapper<>(FlightSegment.class));
    }


    public PlaneRide update(int id, PlaneRide planeRide){
        if(!planeRideExists(id)){
            throw new RuntimeException("The plane ride you are looking for does not exist.");
        }

        String sql = "UPDATE planeRide SET one_way = ?, totalPrice = ?, currency = ?, created_at = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,  planeRide.isOneWay(), planeRide.getTotalPrice(), planeRide.getCurrency(), planeRide.getCreatedAt(), id);

        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the plane ride with id : " + id);
        }

        return this.findById(id);

    }

    public boolean delete(int id) {
        String sql = "DELETE FROM planeRide WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }


    public boolean planeRideExists(int id){
        String sqlCheck = "SELECT COUNT(*) FROM planeRide WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, id);
        return count > 0;
    }

    public PlaneRide save(PlaneRide planeRide) {
        String sql = "INSERT INTO planeRide (one_way, totalPrice, currency, created_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, planeRide.isOneWay(), planeRide.getTotalPrice(), planeRide.getCurrency(), planeRide.getCreatedAt());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        planeRide.setId(id);
        return planeRide;
    }
}
