package com.example.odyssea.daos.mainTables;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItineraryImageDao {
    private final JdbcTemplate jdbc;

    public ItineraryImageDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<String> findRolesByItinerary(int itineraryId) {
        String sql = """
            SELECT role
              FROM itineraryImages
             WHERE itineraryId = ?
            """;
        return jdbc.queryForList(sql, String.class, itineraryId);
    }
}
