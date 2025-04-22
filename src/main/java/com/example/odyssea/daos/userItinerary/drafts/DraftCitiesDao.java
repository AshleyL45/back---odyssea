package com.example.odyssea.daos.userItinerary.drafts;

import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.entities.userItinerary.drafts.DraftCities;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DraftCitiesDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserItineraryDraftDao userItineraryDraftDao;

    public DraftCitiesDao(JdbcTemplate jdbcTemplate, UserItineraryDraftDao userItineraryDraftDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userItineraryDraftDao = userItineraryDraftDao;
    }

    private final RowMapper<DraftCities> draftOptionsRowMapper = (rs, _) -> new DraftCities(
            rs.getInt("draft_user_itinerary_id"),
            rs.getInt("city_id"),
            rs.getInt("position")
    );

    public List<Integer> getCitiesByDraftId(int userId) {
        String sql = """
        SELECT dc.city_id
         FROM draft_cities dc
         JOIN userItineraryDraft uid ON dc.draft_user_itinerary_id = uid.id
         WHERE uid.user_id = ?
    """;
        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }

    public void saveCities(Integer userId, List<Integer> citiesIds){
        Integer draftId = userItineraryDraftDao.getLastDraftIdByUser(userId);
        String sql = "INSERT INTO draft_cities (draft_user_itinerary_id, city_id, position) VALUES (?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < citiesIds.size(); i++) {
            batchArgs.add(new Object[] {draftId, citiesIds.get(i), i + 1});
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public List<City> findDraftCities(Integer userId){
        Integer draftId = userItineraryDraftDao.getLastDraftIdByUser(userId);
        String sql = "SELECT city.* FROM draft_cities\n" +
                "INNER JOIN city ON draft_cities.city_id = city.id WHERE draft_cities.draft_user_itinerary_id = ? ";

        return jdbcTemplate.query(sql, new Object[]{draftId}, new BeanPropertyRowMapper<>(City.class));
    }
}
