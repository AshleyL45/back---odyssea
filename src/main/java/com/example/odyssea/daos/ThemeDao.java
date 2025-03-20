package com.example.odyssea.daos;

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

    // Recherche les thèmes dont le nom contient la chaîne de caractères spécifiée
    public List<Theme> searchTheme(String query) {
        String sql = "SELECT * FROM theme WHERE LOWER(name) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, themeRowMapper, "%" + query + "%");
    }

    // Récupère un thème par son ID
    public Theme findById(int idTheme) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        return jdbcTemplate.query(sql, themeRowMapper, idTheme)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Theme with ID: " + idTheme + " not found"));
    }

    // Sauvegarde un nouveau thème dans la base de données et renvoie l'objet avec l'ID généré
    public Theme save(Theme theme) {
        String sql = "INSERT INTO theme (name) VALUES (?)";
        jdbcTemplate.update(sql, theme.getThemeName());

        // Récupère l'ID généré automatiquement après l'insertion
        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        theme.setThemeId(id);
        return theme;
    }

    // Met à jour un thème existant identifié par son ID
    public Theme update(int idTheme, Theme theme) {
        if (!themeExists(idTheme)) {
            throw new RuntimeException("Theme with ID: " + idTheme + " not found");
        }

        String sql = "UPDATE theme SET name = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, theme.getThemeName(), idTheme);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Failed to update theme with ID: " + idTheme);
        }

        return this.findById(idTheme);
    }

    // Vérifie si un thème existe dans la base de données en fonction de son ID
    private boolean themeExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM theme WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }

    // Supprime un thème de la base de données en fonction de son ID
    public boolean delete(int id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
}
