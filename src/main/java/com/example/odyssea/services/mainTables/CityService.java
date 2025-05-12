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
    public City getCityById(int id) {
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
    public void updateCity(int id, City city) {
        cityDao.findById(id);
        city.setId(id);
        cityDao.update(city);
    }

    /**
     * Supprime une ville par son identifiant
     */
    public void deleteCity(int id) {
        cityDao.findById(id);
        cityDao.deleteById(id);
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
    public City getCityByCoordinates(double latitude, double longitude) {
        return cityDao.findByCoordinates(latitude, longitude);
    }

    /**
     * Obtient uniquement les coordonnées d'une ville en fonction de son ID
     */
    public double[] getCityCoordinates(int id) {
        City city = cityDao.findById(id); // on récupère la ville directement
        return new double[]{city.getLatitude(), city.getLongitude()};
    }
}
