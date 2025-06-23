package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.Image;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageDao {
    private final JdbcTemplate jdbc;

    public ImageDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    RowMapper<Image> rowMapper = (rs, rowNum) -> new Image(
            rs.getInt("id"),
            rs.getInt("itinerary_id"),
            rs.getString("size_type"),
            rs.getString("link"),
            rs.getString("alt_text")
    );

    public Image findByItineraryAndRole(int itineraryId, String role) {
        String sql = """
            SELECT i.*
              FROM images i
              JOIN itinerary_images ii
                ON ii.image_id = i.id
             WHERE ii.itinerary_id = ?
               AND ii.role = ?
            """;
        try {
            return jdbc.queryForObject(sql, rowMapper, itineraryId, role);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public List<String> findRolesByItinerary(int itineraryId) {
        String sql = """
            SELECT role
              FROM itinerary_images
             WHERE itinerary_id = ?
            """;
        return jdbc.queryForList(sql, String.class, itineraryId);
    }
}
