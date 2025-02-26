package com.example.odyssea.daos;

import com.example.odyssea.entities.Country;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CountryDao {

    private final JdbcTemplate jdbcTemplate;

    public CountryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 🔹 Vérifie si un pays avec l'ID spécifié existe dans la base de données
     */
    public boolean existsById(int countryId) {
        String sql = "SELECT COUNT(*) FROM country WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{countryId}, Integer.class);
        return count != null && count > 0;
    }

    /**
     * 🔹 Obtenir un pays en ayant le nom d'une ville
     */
    public Optional<Country> findByCityName(String cityName) {
        String sql = """
            SELECT c.* FROM country c
            JOIN city ct ON c.id = ct.country_id
            WHERE ct.name = ?
        """;
        return jdbcTemplate.query(sql, new Object[]{cityName}, new CountryRowMapper())
                .stream()
                .findFirst();
    }

    /**
     * 🔹 Obtenir tous les pays en fonction du continent
     */
    public List<Country> findByContinent(String continent) {
        String sql = "SELECT * FROM country WHERE continent = ?";
        return jdbcTemplate.query(sql, new Object[]{continent}, new CountryRowMapper());
    }

    /**
     * 🔹 Obtenir tous les continents disponibles
     */
    public List<String> getAllContinents() {
        String sql = "SELECT DISTINCT continent FROM country";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    /**
     * 🔹 Rechercher un pays par mot-clé (recherche partielle)
     */
    public List<Country> searchByKeyword(String keyword) {
        String sql = "SELECT * FROM country WHERE LOWER(name) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, new Object[]{"%" + keyword + "%"}, new CountryRowMapper());
    }

    /**
     * 🔹 Mapper pour convertir un `ResultSet` en objet `Country`
     */
    private static class CountryRowMapper implements RowMapper<Country> {
        @Override
        public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Country(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("continent"),
                    rs.getDouble("price")
            );
        }
    }
}
