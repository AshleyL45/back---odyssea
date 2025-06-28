package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.exceptions.HotelAlreadyExistsException;
import com.example.odyssea.exceptions.HotelNotFound;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        String sql = "INSERT INTO hotel (city_id, name, star_rating, description, price) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, hotel.getCityId());
            ps.setString(2, hotel.getName());
            ps.setInt(3, hotel.getStarRating());
            ps.setString(4, hotel.getDescription());
            ps.setDouble(5, hotel.getPrice());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            hotel.setId(((Number) keyHolder.getKey()).intValue());
        }
    }


    public Hotel findByNameAndCityId(String name, int cityId) {
        String sql = "SELECT * FROM hotel WHERE name = ? AND city_id = ?";

        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{name, cityId},
                (rs, rowNum) -> {
                    Hotel h = new Hotel();
                    h.setId(rs.getInt("id"));
                    h.setName(rs.getString("name"));
                    h.setCityId(rs.getInt("city_id"));
                    h.setStarRating(rs.getInt("star_rating"));
                    h.setDescription(rs.getString("description"));
                    h.setPrice(rs.getDouble("price"));
                    return h;
                }
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
