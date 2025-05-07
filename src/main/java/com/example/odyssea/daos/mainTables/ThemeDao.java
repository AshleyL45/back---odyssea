package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ThemeDao {

    // Injection de JdbcTemplate pour l'accès à la base de données
    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Theme> themeRowMapper = (rs, rowNum) -> new Theme(
            rs.getInt("id"),
            rs.getString("name")
    );

    // Récupère tous les thèmes de la base de données
    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    // Récupère un thème par son ID
    public Theme findById(int idTheme) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        return jdbcTemplate.query(sql, themeRowMapper, idTheme)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Theme with ID: " + idTheme + " not found"));
    }
}
