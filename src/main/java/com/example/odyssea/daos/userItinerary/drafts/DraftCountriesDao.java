package com.example.odyssea.daos.userItinerary.drafts;

import com.example.odyssea.entities.userItinerary.drafts.DraftCountries;
import com.example.odyssea.entities.userItinerary.drafts.DraftOptions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
            rs.getInt("country_id")
    );

    public List<Integer> saveCountries(Integer userId, List<Integer> countries){
        Integer draftId = userItineraryDraftDao.getLastDraftIdByUser(userId);
        String sql = "UPDATE draft_countries SET draft_user_itinerary_id = ? AND country_id = ?";
    }

}
