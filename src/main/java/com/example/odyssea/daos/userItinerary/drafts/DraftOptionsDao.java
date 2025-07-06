package com.example.odyssea.daos.userItinerary.drafts;

import com.example.odyssea.entities.mainTables.Country;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.userItinerary.drafts.DraftOptions;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DraftOptionsDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserItineraryDraftDao userItineraryDraftDao;

    public DraftOptionsDao(JdbcTemplate jdbcTemplate, UserItineraryDraftDao userItineraryDraftDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userItineraryDraftDao = userItineraryDraftDao;
    }

    private final RowMapper<DraftOptions> draftOptionsRowMapper = (rs, _) -> new DraftOptions(
            rs.getInt("draft_user_itinerary_id"),
            rs.getInt("option_id")
    );

    public List<Option> findAllByUserItineraryId(Integer useItineraryId){
        String sql = "SELECT options.* FROM draft_options INNER JOIN options ON draft_options.option_id = options.id";
        return jdbcTemplate.query(sql, new Object[]{useItineraryId}, new BeanPropertyRowMapper<>(Option.class));
    }

    public void saveOptions(Integer userId, List<Integer> optionIds){
        Integer draftId = userItineraryDraftDao.getLastDraftIdByUser(userId);
        String sql = "INSERT INTO draft_options (draft_user_itinerary_id, option_id) VALUES (?, ?)";
        deleteOptionsByDraftId(draftId);

        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < optionIds.size(); i++) {
            batchArgs.add(new Object[] {draftId, optionIds.get(i)});
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);

    }

    public List<Option> findDraftOptions(Integer userId){
        Integer draftId = userItineraryDraftDao.getLastDraftIdByUser(userId);
        String sql = "SELECT options.* FROM draft_options\n" +
                "INNER JOIN options ON draft_options.option_id = options.id WHERE draft_options.draft_user_itinerary_id = ? ";

        return jdbcTemplate.query(sql, new Object[]{draftId}, new BeanPropertyRowMapper<>(Option.class));
    }


    private void deleteOptionsByDraftId(Integer draftId) {
        String deleteSql = "DELETE FROM draft_options WHERE draft_user_itinerary_id = ?";
        jdbcTemplate.update(deleteSql, draftId);
    }
}
