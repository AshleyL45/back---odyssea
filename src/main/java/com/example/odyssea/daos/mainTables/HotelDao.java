package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.exceptions.HotelAlreadyExistsException;
import com.example.odyssea.exceptions.HotelNotFound;
import com.example.odyssea.exceptions.ResourceNotFoundException;
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

    public boolean cityExists(int cityId) {
        String sql = "SELECT COUNT(*) FROM city WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cityId);
        return count != null && count > 0;
    }

    public boolean existsById(int id) {
        String sql = "SELECT COUNT(*) FROM hotel WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public void save(Hotel hotel) {
        if (!cityExists(hotel.getCityId())) {
            throw new ResourceNotFoundException("City with id " + hotel.getCityId() + " not found.");
        }
        String checkSql = "SELECT COUNT(*) FROM hotel WHERE name = ? AND city_id = ? AND star_rating = ?";
        Integer count = jdbcTemplate.queryForObject(
                checkSql,
                Integer.class,
                hotel.getName(),
                hotel.getCityId(),
                hotel.getStarRating()
        );
        if (count != null && count > 0) {
            throw new HotelAlreadyExistsException(
                    "Hotel \"" + hotel.getName() +
                            "\" already exists for cityId=" + hotel.getCityId() +
                            " with starRating=" + hotel.getStarRating()
            );
        }

        String sql = "INSERT INTO hotel "
                + "(city_id, name, star_rating, description, price) "
                + "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                hotel.getCityId(),
                hotel.getName(),
                hotel.getStarRating(),
                hotel.getDescription(),
                hotel.getPrice()
        );
    }



    public void update(Hotel hotel) {
        if (!cityExists(hotel.getCityId())) {
            throw new ResourceNotFoundException("City with id " + hotel.getCityId() + " not found.");
        }
        String sql = "UPDATE hotel SET city_id = ?, name = ?, star_rating = ?, description = ?, price = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                hotel.getCityId(),
                hotel.getName(),
                hotel.getStarRating(),
                hotel.getDescription(),
                hotel.getPrice(),
                hotel.getId()
        );
        if (rowsAffected == 0) {
            throw new HotelNotFound("Hotel with id " + hotel.getId() + " not found for update.");
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM hotel WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new HotelNotFound("Hotel with id " + id + " not found for deletion.");
        }
    }

    public Optional<Hotel> findById(int id) {
        String sql = "SELECT * FROM hotel WHERE id = ?";
        List<Hotel> hotels = jdbcTemplate.query(sql, new HotelRowMapper(), id);
        if (hotels.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(hotels.get(0));
    }

    public List<Hotel> findAll() {
        String sql = "SELECT * FROM hotel";
        List<Hotel> hotels = jdbcTemplate.query(sql, new HotelRowMapper());
        if (hotels.isEmpty()) {
            throw new HotelNotFound("No hotels found.");
        }
        return hotels;
    }

    public List<Hotel> findByCityId(int cityId) {
        String sql = "SELECT * FROM hotel WHERE city_id = ?";
        List<Hotel> hotels = jdbcTemplate.query(sql, new HotelRowMapper(), cityId);
        if (hotels.isEmpty()) {
            throw new HotelNotFound("No hotels found for city id " + cityId);
        }
        return hotels;
    }

    public boolean existsByNameAndCityId(String name, int cityId) {
        String sql = "SELECT COUNT(*) FROM hotel WHERE name = ? AND city_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name, cityId);
        return count != null && count > 0;
    }

    public List<Hotel> findByCityIdAndStarRating(int cityId, int starRating) {
        String sql = "SELECT * FROM hotel WHERE city_id = ? AND star_rating = ?";
        List<Hotel> hotels = jdbcTemplate.query(sql, new HotelRowMapper(), cityId, starRating);
        if (hotels.isEmpty()) {
            throw new HotelNotFound("No hotels found with star rating " + starRating + " in city id " + cityId);
        }
        return hotels;
    }

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
