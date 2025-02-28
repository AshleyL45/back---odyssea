package com.example.odyssea.daos;

import com.example.odyssea.entities.mainTables.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private final RowMapper<Theme> themeRowMapper = (rs, rowNum) -> new Theme(
            rs.getInt("id"),
            rs.getInt("itineraryId"),
            rs.getString("name")
    );



    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper);
    }


    public List<Theme> searchTheme(String query){
        String sql = "SELECT * FROM theme WHERE LOWER(name) LIKE LOWER(?) ";
        return jdbcTemplate.query(sql, themeRowMapper, "%" + query + "%");
    }


    public Theme findById(int idTheme) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        return jdbcTemplate.query(sql, themeRowMapper, idTheme)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Theme avec l'ID : " + idTheme + " n'existe pas"));
    }


    public Theme save(Theme theme) {
        String sql = "INSERT INTO theme (itineraryId, name) VALUES (?, ?)";
        jdbcTemplate.update(sql, theme.getIdItinerary(), theme.getThemeName());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        theme.setIdTheme(id);
        return theme;
    }



    public Theme update(int idTheme, Theme theme) {
        if (!themeExists(idTheme)) {
            throw new RuntimeException("Thème avec l'ID : " + idTheme + " n'existe pas");
        }

        String sql = "UPDATE theme SET itineraryId = ?, name = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, theme.getIdItinerary(), theme.getThemeName(), idTheme);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour du thème avec l'ID : " + idTheme);
        }

        return this.findById(idTheme);
    }


    private boolean themeExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM theme WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }



    public boolean delete(int id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }


}
