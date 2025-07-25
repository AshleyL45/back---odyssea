package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.exceptions.CityNotFound;
import com.example.odyssea.exceptions.DatabaseException;
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
public class CityDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Insertion d'une nouvelle ville
     */
    public void save(City city) {
        String sql = "INSERT INTO city (country_id, name, iata_code, longitude, latitude) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, city.getCountryId(), city.getName(), city.getIataCode(), city.getLongitude(), city.getLatitude());
    }

    /**
     * Mise à jour d'une ville existante
     */
    public void update(City city) {
        String sql = "UPDATE city SET country_id = ?, name = ?, iata_code = ?, longitude = ?, latitude = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                city.getCountryId(), city.getName(), city.getIataCode(),
                city.getLongitude(), city.getLatitude(), city.getId());

        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("City with id " + city.getId() + " not found for update.");
        }
    }

    /**
     * Suppression d'une ville par son ID
     */
    public void deleteById(int id) {
        String sql = "DELETE FROM city WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("City with id " + id + " not found for deletion.");
        }
    }

    /**
     * Recherche d'une ville par son ID
     */
    public City findById(int id) {
        String sql = "SELECT * FROM city WHERE id = ?";
        return jdbcTemplate.query(sql, new CityRowMapper(), id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new CityNotFound("City with id " + id + " not found."));
    }

    /**
     * Récupération de toutes les villes
     */
    public List<City> findAll() {
        String sql = "SELECT * FROM city";
        return jdbcTemplate.query(sql, new CityRowMapper());
    }

    /**
     * Récupère toutes les villes appartenant à un pays donné
     */
    public List<City> findByCountryId(int countryId) {
        String sql = "SELECT * FROM city WHERE country_id = ?";
        List<City> cities = jdbcTemplate.query(sql, new CityRowMapper(), countryId);
        if (cities.isEmpty()) {
            throw new ResourceNotFoundException("No cities found for country with id " + countryId);
        }
        return cities;
    }

    /**
     * Retrouve une ville en fonction de ses coordonnées exactes (latitude et longitude)
     */
    public City findByCoordinates(double latitude, double longitude) {
        String sql = "SELECT * FROM city WHERE latitude = ? AND longitude = ?";
        return jdbcTemplate.query(sql, new CityRowMapper(), latitude, longitude)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "City not found for coordinates: latitude " + latitude + ", longitude " + longitude));
    }

    /**
     * Retrouver le code IATA et l'id en fonction du nom de la ville
     */
    public City findCityByName(String cityName) {
        String sql = "SELECT * FROM city WHERE name = ?";
        return jdbcTemplate.query(sql, new CityRowMapper(), cityName)
                .stream()
                .findFirst()
                .orElseThrow(() -> new CityNotFound("The city '" + cityName + "' doesn't exist."));
    }

    /**
     * Obtient uniquement les coordonnées (latitude et longitude) d'une ville donnée par son ID
     */
    public double[] getCoordinatesByCityId(int id) {
        City city = findById(id);
        return new double[]{city.getLatitude(), city.getLongitude()};
    }

    /**
     * Récupère le code IATA d'une ville à partir de son ID.
     */
    public String getIataCodeById(int cityId) {
        String sql = "SELECT iata_code FROM city WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, cityId);
    }

    /**
     * RowMapper pour transformer un ResultSet en objet City
     */
    private static class CityRowMapper implements RowMapper<City> {
        @Override
        public City mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new City(
                    rs.getInt("id"),
                    rs.getInt("country_id"),
                    rs.getString("name"),
                    rs.getString("iata_code"),
                    rs.getDouble("longitude"),
                    rs.getDouble("latitude")
            );
        }
    }
}
