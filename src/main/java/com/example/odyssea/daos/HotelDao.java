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

    // Récupérer les hôtels d'une ville
    public List<Hotel> findByCityId(int cityId) {
        String sql = "SELECT * FROM hotels WHERE city_id = ?";
        return jdbcTemplate.query(sql, new Object[]{cityId}, new HotelRowMapper());
    }

    // Récupérer les hôtels d'une ville avec un standing spécifique
    public List<Hotel> findByCityIdAndStarRating(int cityId, int starRating) {
        String sql = "SELECT * FROM hotels WHERE city_id = ? AND star_rating = ?";
        return jdbcTemplate.query(sql, new Object[]{cityId, starRating}, new HotelRowMapper());
    }

    // Récupérer les hôtels par plage de prix
    public List<Hotel> findByPriceRange(double minPrice, double maxPrice) {
        String sql = "SELECT * FROM hotels WHERE price BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{minPrice, maxPrice}, new HotelRowMapper());
    }

    // Récupérer un hôtel par son nom
    public Optional<Hotel> findByName(String hotelName) {
        String sql = "SELECT * FROM hotels WHERE name LIKE ?";
        return jdbcTemplate.query(sql, new Object[]{"%" + hotelName + "%"}, new HotelRowMapper())
                .stream().findFirst();
    }

    // Vérifier si un hôtel existe en base
    public boolean existsById(int hotelId) {
        String sql = "SELECT COUNT(*) FROM hotels WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, hotelId);
        return count != null && count > 0;
    }

    // Sauvegarder un hôtel en base
    public void save(Hotel hotel) {
        String sql = "INSERT INTO hotels (id, city_id, name, star_rating, description, price) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, hotel.getId(), hotel.getCityId(), hotel.getName(), hotel.getStarRating(), hotel.getDescription(), hotel.getPrice());
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
