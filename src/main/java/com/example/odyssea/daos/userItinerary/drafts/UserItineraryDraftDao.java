package com.example.odyssea.daos.userItinerary.drafts;

import com.example.odyssea.entities.userItinerary.drafts.UserItineraryDraft;
import com.example.odyssea.exceptions.DatabaseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class UserItineraryDraftDao {
    private final JdbcTemplate jdbcTemplate;


    public UserItineraryDraftDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserItineraryDraft> userItineraryDraftRowMapper = (rs, _) -> new UserItineraryDraft(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getInt("duration"),
            rs.getObject("start_date", LocalDate.class),
            rs.getString("departure_city"),
            rs.getInt("hotel_standing"),
            rs.getInt("number_adults"),
            rs.getInt("number_kids"),
            rs.getObject("created_at", LocalDate.class)
    );

    public UserItineraryDraft getLastDraftByUserId(Integer userId){
        String sql = "SELECT * FROM user_itinerary_draft WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
        return jdbcTemplate.query(sql, userItineraryDraftRowMapper, userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new DatabaseException("No draft found for user with ID : " + userId));
    }

    public Integer getLastDraftIdByUser(int userId) {
        try {
            String sql = "SELECT id FROM user_itinerary_draft WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
            return jdbcTemplate.queryForObject(sql, Integer.class, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new DatabaseException("No draft found for user with ID: " + userId);
        } catch (Exception e) {
            throw new DatabaseException("Error while retrieving the last draft: " + e.getMessage());
        }
    }

    public Integer getDurationByUserId(int userId) {
        String sql = "SELECT duration FROM user_itinerary_draft WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new DatabaseException("No itinerary found for the user with ID: " + userId);
        }
    }

    public Integer saveFirstStep(int userId, int duration){
        try {
            String sql = "INSERT INTO user_itinerary_draft (user_id, duration) VALUES (?, ?)";
            jdbcTemplate.update(sql, userId, duration);

            String sqlGetId = "SELECT LAST_INSERT_ID()";
            Integer generatedId = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

            if (generatedId == null) {
                throw new DatabaseException("Cannot get draft's id");
            }

            return generatedId;
        } catch (Exception e) {
            throw new DatabaseException("An error occurred while saving the draft : " + e.getMessage());
        }
    }

    public LocalDate saveDate(int userId, LocalDate startDate){
        int draftId = getLastDraftIdByUser(userId);

        String sql = "UPDATE user_itinerary_draft SET start_date = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, startDate, draftId);

        if (rowsAffected == 0) {
            throw new DatabaseException("No draft found with ID: " + draftId);
        }

        return startDate;
    }

    public String saveDepartureCity(int userId, String departureCity){
        int draftId = getLastDraftIdByUser(userId);

        String sql = "UPDATE user_itinerary_draft SET departure_city = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, departureCity, draftId);

        if (rowsAffected == 0) {
            throw new DatabaseException("No draft found with ID: " + draftId);
        }

        return departureCity;

    }

    public void saveHotelStanding(int userId, int hotelStanding){
        int draftId = getLastDraftIdByUser(userId);

        String sql = "UPDATE user_itinerary_draft SET hotel_standing = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, hotelStanding, draftId);

        if (rowsAffected == 0) {
            throw new DatabaseException("No draft found with ID: " + draftId);
        }

    }


    public void saveTravelsNumber(int userId, int numberAdults, int numberKids){
        int draftId = getLastDraftIdByUser(userId);

        String sql = "UPDATE user_itinerary_draft SET number_adults = ?, number_kids = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, numberAdults, numberKids, draftId);

        if (rowsAffected == 0) {
            throw new DatabaseException("No draft found with ID: " + draftId);
        }
    }

    public boolean delete(Integer id){
        boolean doesExist = doesExist(id);
        if(!doesExist){
            return false;
        }
        String sql = "DELETE FROM user_itinerary_draft WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        return rowsAffected > 0;
    }

    public boolean doesExist(Integer id){
        String sql = "SELECT COUNT (*) FROM user_itinerary_draft WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count > 0;
    }
}
