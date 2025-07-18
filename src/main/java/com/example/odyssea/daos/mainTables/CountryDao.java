package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.Country;
import com.example.odyssea.exceptions.CountryNotFound;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public CountryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Enregistre un nouveau pays
     */
    public void save(Country country) {
        String sql = "INSERT INTO country (name, continent, price) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, country.getName(), country.getContinent(), country.getPrice());
    }

    /**
     * Met à jour un pays existant
     */
    public void update(Country country) {
        String sql = "UPDATE country SET name = ?, continent = ?, price = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, country.getName(), country.getContinent(), country.getPrice(), country.getId());
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Country with id " + country.getId() + " not found for update.");
        }
    }

    /**
     * Supprime un pays par son identifiant
     */
    public void deleteById(int id) {
        String sql = "DELETE FROM country WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Country with id " + id + " not found for deletion.");
        }
    }

    /**
     * Recherche un pays par son identifiant
     */
    public Country findById(int id) {
        String sql = "SELECT * FROM country WHERE id = ?";
        return jdbcTemplate.query(sql, new CountryRowMapper(), id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new CountryNotFound("Country with id " + id + " not found."));
    }

    /**
     * Récupère tous les pays
     */
    public List<Country> findAll() {
        String sql = "SELECT * FROM country";
        return jdbcTemplate.query(sql, new CountryRowMapper());
    }

    /**
     * Recherche les pays appartenant à un continent donné
     */
    public List<Country> findByContinent(String continent) {
        String sql = "SELECT * FROM country WHERE continent = ?";
        List<Country> countries = jdbcTemplate.query(sql, new CountryRowMapper(), continent);
        if (countries.isEmpty()) {
            throw new ResourceNotFoundException("No countries found for continent " + continent);
        }
        return countries;
    }

    /**
     * Recherche le pays associé à une ville donnée par son nom
     */
    public Country findByCityName(String cityName) {
        String sql = "SELECT c.* FROM country c JOIN city ct ON c.id = ct.country_id WHERE ct.name = ?";
        return jdbcTemplate.query(sql, new CountryRowMapper(), cityName)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No country found for city name " + cityName));
    }

    /**
     * Retrouve un pays à partir de son nom
     */
    public Country findByName(String countryName) {
        String sql = "SELECT * FROM country WHERE name = ?";
        return jdbcTemplate.query(sql, new CountryRowMapper(), countryName)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("The country " + countryName + " isn't available"));
    }

    /**
     * RowMapper pour transformer un ResultSet en objet Country
     */
    private static class CountryRowMapper implements RowMapper<Country> {
        @Override
        public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Country(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("continent"),
                    rs.getBigDecimal("price")
            );
        }
    }
}
