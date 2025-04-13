package com.example.odyssea.daos.userItinerary.drafts;

import com.example.odyssea.entities.userItinerary.drafts.DraftCountries;
import com.example.odyssea.entities.userItinerary.drafts.DraftOptions;
import com.example.odyssea.exceptions.DatabaseException;
import org.springframework.dao.EmptyResultDataAccessException;
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


    public void saveCountries(Integer userId, List<Integer> countriesIds){
        Integer draftId = userItineraryDraftDao.getLastDraftIdByUser(userId);
        String sql = "INSERT INTO draft_countries (draft_user_itinerary_id, country_id, position) VALUES (?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < countriesIds.size(); i++) {
            batchArgs.add(new Object[] {draftId, countriesIds.get(i), i + 1});
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

}
