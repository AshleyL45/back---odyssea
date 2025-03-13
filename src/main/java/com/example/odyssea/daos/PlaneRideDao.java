package com.example.odyssea.daos;

import com.example.odyssea.entities.mainTables.Flight;
import com.example.odyssea.entities.mainTables.FlightSegment;
import com.example.odyssea.entities.mainTables.PlaneRide;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlaneRideDao {
    private final JdbcTemplate jdbcTemplate;


    public PlaneRideDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PlaneRide> planeRideRowMapper = (rs, _) -> new PlaneRide(
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

        // Utiliser 'Statement.RETURN_GENERATED_KEYS' pour récupérer l'ID généré
        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Paramétrage des valeurs dans la requête
            stmt.setBoolean(1, planeRide.isOneWay());
            stmt.setBigDecimal(2, planeRide.getTotalPrice());
            stmt.setString(3, planeRide.getCurrency());
            stmt.setTimestamp(4, Timestamp.valueOf(planeRide.getCreatedAt()));

            // Exécution de l'insertion
            int rowsAffected = stmt.executeUpdate();

            // Si l'insertion est réussie, récupérer l'ID généré
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        planeRide.setId(generatedKeys.getInt(1));  // Récupérer l'ID généré
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du PlaneRide", e);
        }

        return planeRide;
    }


    public void saveAll(List<PlaneRide> planeRides) {
        String sql = "INSERT INTO planeRide (one_way, totalPrice, currency, created_at) VALUES (?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (PlaneRide planeRide : planeRides) {
            batchArgs.add(new Object[]{ // Contient les valeurs (sous forme de tableau) à insérer dans la BDD
                    planeRide.isOneWay(),
                    planeRide.getTotalPrice(),
                    planeRide.getCurrency(),
                    planeRide.getCreatedAt()
            });
        }

        jdbcTemplate.batchUpdate(sql, batchArgs); // BatchUpdate attend pour chaque ligne de la table un objet avec les valeurs dans un tableau
    }
}
