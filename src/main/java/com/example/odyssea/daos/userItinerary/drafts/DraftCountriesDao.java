package com.example.odyssea.daos.userItinerary.drafts;

import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.entities.mainTables.Country;
import com.example.odyssea.entities.userItinerary.drafts.DraftCountries;
import com.example.odyssea.entities.userItinerary.drafts.DraftOptions;
import com.example.odyssea.exceptions.DatabaseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class DraftCountriesDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserItineraryDraftDao userItineraryDraftDao;

    public DraftCountriesDao(JdbcTemplate jdbcTemplate, UserItineraryDraftDao userItineraryDraftDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userItineraryDraftDao = userItineraryDraftDao;
    }

    private final RowMapper<DraftCountries> draftCountriesRowMapper = (rs, _) -> new DraftCountries(
            rs.getInt("draft_user_itinerary_id"),
            rs.getInt("country_id"),
            rs.getInt("position")
    );

    public List<Integer> getCountriesByDraftId(int userId) {
        String sql = """
        SELECT draft_countries.country_id
         FROM draft_countries
         JOIN user_itinerary_draft ON draft_countries.draft_user_itinerary_id = user_itinerary_draft.id
         WHERE user_itinerary_draft.user_id = ?
    """;
        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }


    public void saveCountries(Integer userId, List<Integer> countriesIds){
        Integer draftId = userItineraryDraftDao.getLastDraftIdByUser(userId);
        String sql = "INSERT INTO draft_countries (draft_user_itinerary_id, country_id, position) VALUES (?, ?, ?)";
        deleteCountriesByDraftId(draftId);

        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < countriesIds.size(); i++) {
            batchArgs.add(new Object[] {draftId, countriesIds.get(i), i + 1});
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public List<Country> findDraftCountries(Integer userId){
        Integer draftId = userItineraryDraftDao.getLastDraftIdByUser(userId);
        String sql = "SELECT country.* FROM draft_countries\n" +
                "INNER JOIN country ON draft_countries.country_id = country.id WHERE draft_countries.draft_user_itinerary_id = ? ";

        return jdbcTemplate.query(sql, new Object[]{draftId}, new BeanPropertyRowMapper<>(Country.class));
    }


    private void deleteCountriesByDraftId(Integer draftId) {
        String deleteSql = "DELETE FROM draft_countries WHERE draft_user_itinerary_id = ?";
        jdbcTemplate.update(deleteSql, draftId);
    }
}
