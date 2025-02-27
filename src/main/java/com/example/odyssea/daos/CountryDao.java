package com.example.odyssea.daos;

import com.example.odyssea.entities.mainTables.Country;
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
     * 🔹 Sauvegarde un nouveau pays
     */
    public void save(Country country) {
        String sql = """
            INSERT INTO country (name, continent, price)
            VALUES (?, ?, ?)
        """;
        jdbcTemplate.update(sql, country.getName(), country.getContinent(), country.getPrice());
    }

    /**
     * 🔹 Met à jour un pays existant
     */
    public void update(Country country) {
        String sql = """
            UPDATE country 
            SET name = ?, continent = ?, price = ? 
            WHERE id = ?
        """;
        jdbcTemplate.update(sql, country.getName(), country.getContinent(), country.getPrice(), country.getId());
    }

    /**
     * 🔹 Supprime un pays par ID
     */
    public void deleteById(int countryId) {
        String sql = "DELETE FROM country WHERE id = ?";
        jdbcTemplate.update(sql, countryId);
    }

    /**
     * 🔹 Vérifie si un pays existe par son ID
     */
    public boolean existsById(int countryId) {
        String sql = "SELECT COUNT(*) FROM country WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{countryId}, Integer.class);
        return count != null && count > 0;
    }

    /**
     * 🔹 Récupère un pays par son ID
     */
    public Optional<Country> findById(int countryId) {
        String sql = "SELECT * FROM country WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{countryId}, new CountryRowMapper())
                .stream()
                .findFirst();
    }

    /**
     * 🔹 Récupère tous les pays
     */
    public List<Country> findAll() {
        String sql = "SELECT * FROM country";
        return jdbcTemplate.query(sql, new CountryRowMapper());
    }

    /**
     * 🔹 Compte le nombre total de pays
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM country";
        return jdbcTemplate.queryForObject(sql, Integer.class);
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
