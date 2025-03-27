package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.entities.mainTables.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityDao cityDao;

    @Autowired
    public CityService(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    /**
     * Récupère la liste complète de toutes les villes disponibles dans la base de données
     */
    public List<City> getAllCities() {
        return cityDao.findAll();
    }

    /**
     * Récupère une ville spécifique par son identifiant
     */
    public Optional<City> getCityById(int id) {
        return cityDao.findById(id);
    }

    /**
     * Crée une nouvelle ville dans la base de données
     */
    public void createCity(City city) {
        cityDao.save(city);
    }

    /**
     * Met à jour les informations d'une ville existante
     */
    public boolean updateCity(int id, City city) {
        Optional<City> existing = cityDao.findById(id);
        if (existing.isEmpty()) {
            return false;
        }
        city.setId(id);
        cityDao.update(city);
        return true;
    }

    /**
     * Supprime une ville par son identifiant
     */
    public boolean deleteCity(int id) {
        Optional<City> existing = cityDao.findById(id);
        if (existing.isEmpty()) {
            return false;
        }
        cityDao.deleteById(id);
        return true;
    }

    /**
     * Récupère toutes les villes appartenant à un pays donné
     */
    public List<City> getCitiesByCountryId(int countryId) {
        return cityDao.findByCountryId(countryId);
    }

    /**
     * Retrouve une ville par ses coordonnées géographiques
     */
    public Optional<City> getCityByCoordinates(double latitude, double longitude) {
        return cityDao.findByCoordinates(latitude, longitude);
    }

    /**
     * Obtient uniquement les coordonnées d'une ville en fonction de son ID
     */
    public Optional<double[]> getCityCoordinates(int id) {
        return cityDao.getCoordinatesByCityId(id);
    }
}
