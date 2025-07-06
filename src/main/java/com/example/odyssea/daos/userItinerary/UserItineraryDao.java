package com.example.odyssea.daos.userItinerary;

import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.enums.BookingStatus;
import com.example.odyssea.exceptions.UserItineraryDatabaseException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class UserItineraryDao {


    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(UserItineraryDao.class);
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("start_date", "booking_date");


    public UserItineraryDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<UserItinerary> userItineraryRowMapper = (rs, _) -> new UserItinerary(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getDate("start_date").toLocalDate(),
            rs.getDate("end_date").toLocalDate(),
            rs.getBigDecimal("starting_price"),
            rs.getInt("total_duration"),
            rs.getString("departure_city"),
            rs.getString("itinerary_name"),
            rs.getInt("number_of_adults"),
            rs.getInt("number_of_kids"),
            rs.getDate("booking_date").toLocalDate(),
            BookingStatus.valueOf(rs.getString("status").toUpperCase())
    );


    public List<UserItinerary> getAllUserItinerariesAndFilter(
            String status,
            String search,
            String sortField,
            String sortDirection
    ) {
        boolean hasStatus = status != null && !status.isBlank();
        boolean hasSearch = search != null && !search.isBlank();
        boolean hasValidSort = sortField != null && ALLOWED_SORT_FIELDS.contains(sortField);

        StringBuilder sql = new StringBuilder("SELECT * FROM user_itinerary ");
        Map<String, Object> params = new HashMap<>();

        if (hasSearch) {
            sql.append(" JOIN user u ON user_itinerary.user_id = u.id");
        }

        sql.append(" WHERE 1=1");

        if (hasStatus) {
            sql.append(" AND user_itinerary.status = :status");
            params.put("status", status);
        }

        if (hasSearch) {
            sql.append(" AND (LOWER(u.first_name) LIKE LOWER(:search) OR LOWER(u.last_name) LIKE LOWER(:search))");
            params.put("search", "%" + search + "%");
        }

        if (hasValidSort) {
            sql.append(" ORDER BY ").append(sortField);
            if ("desc".equalsIgnoreCase(sortDirection)) {
                sql.append(" DESC");
            } else {
                sql.append(" ASC");
            }
        }

        return namedParameterJdbcTemplate.query(sql.toString(), new MapSqlParameterSource(params), userItineraryRowMapper);
    }

    public UserItinerary findById(int id){
        String sql = "SELECT * FROM user_itinerary WHERE id = ?";
        return jdbcTemplate.query(sql, userItineraryRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserItineraryDatabaseException("The user itinerary id " + id + " you are looking for does not exist."));
    }

    public List<UserItinerary> findAllUserItineraries(int userId){
        String sql = "SELECT * FROM user_itinerary WHERE user_id = ? ORDER BY booking_date DESC";
        return jdbcTemplate.query(sql, userItineraryRowMapper, userId);
    }

    public UserItinerary save(UserItinerary userItinerary) {
        try {
            String sql = "INSERT INTO user_itinerary (user_id, start_date, end_date, starting_price, total_duration, departure_city, itinerary_name, number_of_adults, number_of_kids, booking_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userItinerary.getUserId());
                ps.setDate(2, Date.valueOf(userItinerary.getStartDate()));
                ps.setDate(3, Date.valueOf(userItinerary.getEndDate()));
                ps.setBigDecimal(4, userItinerary.getStartingPrice());
                ps.setInt(5, userItinerary.getTotalDuration());
                ps.setString(6, userItinerary.getDepartureCity());
                ps.setString(7, userItinerary.getItineraryName());
                ps.setInt(8, userItinerary.getNumberOfAdults());
                ps.setInt(9, userItinerary.getNumberOfKids());
                ps.setDate(10, Date.valueOf(userItinerary.getBookingDate()));
                ps.setString(11, userItinerary.getStatus().name());

                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key == null) {
                throw new UserItineraryDatabaseException("Cannot save user itinerary: generated key is null.");
            }

            userItinerary.setId(key.intValue());
            return userItinerary;
        } catch (Exception e) {
            throw new UserItineraryDatabaseException("Failed to save a personalized trip : " + e.getMessage());
        }
    }


    public UserItinerary update(int id, UserItinerary userItinerary){
        if(!userItineraryExists(id)){
            throw new UserItineraryDatabaseException("The user itinerary you are looking for does not exist.");
        }

        String sql = "UPDATE user_itinerary SET user_id = ?, start_date = ?, end_date = ?, starting_price = ?, total_duration = ?, departure_city = ?, itinerary_name = ?, number_of_adults = ?, number_of_kids = ?, booking_date = ?, status = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItinerary.getUserId(), userItinerary.getStartDate(), userItinerary.getEndDate(), userItinerary.getTotalDuration(), userItinerary.getDepartureCity(), userItinerary.getItineraryName(), userItinerary.getNumberOfAdults(), userItinerary.getNumberOfKids(), userItinerary.getBookingDate(), userItinerary.getStatus().name(), id);

        if(rowsAffected <= 0){
            throw new UserItineraryDatabaseException("Failed to update the user itinerary with id : " + id);
        }

        return this.findById(id);

    }

    public void updateUserItineraryName(int id, String newItineraryName){
        if(!userItineraryExists(id)){
            throw new UserItineraryDatabaseException("The personalized trip ID : " + id + " you are looking for does not exist.");
        }

        String sql = "UPDATE user_itinerary SET itinerary_name = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(sql, newItineraryName, id);
        if(rowsAffected <= 0){
            throw new UserItineraryDatabaseException("Failed to update user itinerary with id : " + id);
        }

    }

    public void updateStatus(int id, BookingStatus newStatus) {
        if (!userItineraryExists(id)) {
            throw new UserItineraryDatabaseException("The personalized trip ID: " + id + " does not exist.");
        }

        String sql = "UPDATE user_itinerary SET status = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, newStatus.name(), id);

        if (rowsAffected <= 0) {
            throw new UserItineraryDatabaseException("Failed to update the status of user itinerary with ID: " + id);
        }
    }

    public void updatePrice(int id, BigDecimal newPrice) {
        if (!userItineraryExists(id)) {
            throw new UserItineraryDatabaseException("The personalized trip ID: " + id + " does not exist.");
        }

        String sql = "UPDATE user_itinerary SET starting_price = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, newPrice, id);

        if (rowsAffected <= 0) {
            throw new UserItineraryDatabaseException("Failed to update the price of user itinerary with ID: " + id);
        }
    }




    public boolean userItineraryExists(int id){
        String sqlCheck = "SELECT COUNT(*) FROM user_itinerary WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, id);
        return count > 0;
    }
}
