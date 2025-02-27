package com.example.odyssea.daos;

import com.example.odyssea.entities.mainTables.Hotel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class HotelDao {

    private final JdbcTemplate jdbcTemplate;

    public HotelDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Récupérer un hôtel par son ID
    public Optional<Hotel> findById(int hotelId) {
        String sql = "SELECT * FROM hotel WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{hotelId}, new HotelRowMapper())
                .stream().findFirst();
    }

    // Récupérer tous les hôtels
    public List<Hotel> findAll() {
        String sql = "SELECT * FROM hotel";
        return jdbcTemplate.query(sql, new HotelRowMapper());
    }

    // Sauvegarder un hôtel (ajout ou mise à jour)
    public void save(Hotel hotel) {
        String sql = "INSERT INTO hotel (id, city_id, name, star_rating, description, price) VALUES (?, ?, ?, ?, ?, ?)"
                + " ON DUPLICATE KEY UPDATE city_id = VALUES(city_id), name = VALUES(name), star_rating = VALUES(star_rating), description = VALUES(description), price = VALUES(price)";
        jdbcTemplate.update(sql, hotel.getId(), hotel.getCityId(), hotel.getName(), hotel.getStarRating(), hotel.getDescription(), hotel.getPrice());
    }

    // Supprimer un hôtel par son ID
    public void deleteById(int hotelId) {
        String sql = "DELETE FROM hotel WHERE id = ?";
        jdbcTemplate.update(sql, hotelId);
    }

    // Mapper pour convertir un ResultSet en objet Hotel
    private static class HotelRowMapper implements RowMapper<Hotel> {
        @Override
        public Hotel mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Hotel(
                    rs.getInt("id"),
                    rs.getInt("city_id"),
                    rs.getString("name"),
                    rs.getInt("star_rating"),
                    rs.getString("description"),
                    rs.getDouble("price")
            );
        }
    }
}
