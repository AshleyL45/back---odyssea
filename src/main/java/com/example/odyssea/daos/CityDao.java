package com.example.odyssea.daos;

import com.example.odyssea.entities.City;
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

    public CityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 🔹 Sauvegarde une nouvelle ville
     */
    public void save(City city) {
        String sql = """
            INSERT INTO city (country_id, name, iata_code, longitude, latitude) 
            VALUES (?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql, city.getCountryId(), city.getName(), city.getIataCode(), city.getLongitude(), city.getLatitude());
    }

    /**
     * 🔹 Mettre à jour une ville existante
     */
    public void update(City city) {
        String sql = """
            UPDATE city 
            SET country_id = ?, name = ?, iata_code = ?, longitude = ?, latitude = ? 
            WHERE id = ?
        """;
        jdbcTemplate.update(sql, city.getCountryId(), city.getName(), city.getIataCode(), city.getLongitude(), city.getLatitude(), city.getId());
    }

    /**
     * 🔹 Supprimer une ville par ID
     */
    public void deleteById(int cityId) {
        String sql = "DELETE FROM city WHERE id = ?";
        jdbcTemplate.update(sql, cityId);
    }

    /**
     * 🔹 Trouver une ville par son ID
     */
    public Optional<City> findById(int cityId) {
        String sql = "SELECT * FROM city WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{cityId}, new CityRowMapper())
                .stream()
                .findFirst();
    }

    /**
     * 🔹 Trouver une ville par son nom
     */
    public Optional<City> findByName(String cityName) {
        String sql = "SELECT * FROM city WHERE name = ?";
        return jdbcTemplate.query(sql, new Object[]{cityName}, new CityRowMapper())
                .stream()
                .findFirst();
    }

    /**
     * 🔹 Trouver une ville par son code IATA
     */
    public Optional<City> findByIataCode(String iataCode) {
        String sql = "SELECT * FROM city WHERE iata_code = ?";
        return jdbcTemplate.query(sql, new Object[]{iataCode}, new CityRowMapper())
                .stream()
                .findFirst();
    }

    /**
     * 🔹 Récupérer toutes les villes d’un pays donné
     */
    public List<City> findByCountryId(int countryId) {
        String sql = "SELECT * FROM city WHERE country_id = ?";
        return jdbcTemplate.query(sql, new Object[]{countryId}, new CityRowMapper());
    }

    /**
     * 🔹 Trouver une ville en fonction de la latitude et de la longitude
     */
    public Optional<City> findByCoordinates(double latitude, double longitude) {
        String sql = "SELECT * FROM city WHERE latitude = ? AND longitude = ?";
        return jdbcTemplate.query(sql, new Object[]{latitude, longitude}, new CityRowMapper())
                .stream()
                .findFirst();
    }

    /**
     * 🔹 Récupérer toutes les villes
     */
    public List<City> findAll() {
        String sql = "SELECT * FROM city";
        return jdbcTemplate.query(sql, new CityRowMapper());
    }

    /**
     * 🔹 Rechercher une ville avec un nom partiel
     */
    public List<City> searchByName(String partialName) {
        String sql = "SELECT * FROM city WHERE LOWER(name) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, new Object[]{"%" + partialName + "%"}, new CityRowMapper());
    }

    /**
     * 🔹 Récupérer les coordonnées GPS d'une ville
     */
    public Optional<double[]> getCoordinatesByCityId(int cityId) {
        String sql = "SELECT latitude, longitude FROM city WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{cityId}, rs -> {
            if (rs.next()) {
                return Optional.of(new double[]{rs.getDouble("latitude"), rs.getDouble("longitude")});
            }
            return Optional.empty();
        });
    }

    /**
     * 🔹 RowMapper pour convertir un `ResultSet` en objet `City`
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
