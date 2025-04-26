package com.example.odyssea.daos.mainTables;

import com.example.odyssea.dtos.mainTables.ItineraryImageDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItineraryImageDao {

    private final JdbcTemplate jdbcTemplate;

    public ItineraryImageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ItineraryImageDto> rowMapper = (rs, rowNum) -> new ItineraryImageDto(
            rs.getInt("imageId"),
            rs.getString("url"),
            rs.getString("alt"),
            rs.getString("role")
    );

    public List<ItineraryImageDto> findByItineraryId(int itineraryId) {
        String sql = """
            SELECT 
              i.id      AS imageId,
              i.link    AS url,
              i.altText AS alt,
              ii.role
            FROM itineraryImages ii
            JOIN images i ON ii.imageId = i.id
            WHERE ii.itineraryId = ?
            ORDER BY 
              CASE ii.role
                WHEN 'firstHeader'   THEN 1
                WHEN 'secondHeader'  THEN 2
                WHEN 'firstCountry'  THEN 3
                WHEN 'secondCountry' THEN 4
                WHEN 'thirdCountry'  THEN 5
                WHEN 'day1'          THEN 6
                WHEN 'day2'          THEN 7
                WHEN 'day3'          THEN 8
                WHEN 'day4'          THEN 9
                WHEN 'day5'          THEN 10
                WHEN 'day6'          THEN 11
                WHEN 'day7'          THEN 12
                WHEN 'day8'          THEN 13
                WHEN 'day9'          THEN 14
                WHEN 'day10'         THEN 15
                WHEN 'day11'         THEN 16
                WHEN 'day12'         THEN 17
                ELSE 99
              END
        """;
        return jdbcTemplate.query(sql, rowMapper, itineraryId);
    }
}
