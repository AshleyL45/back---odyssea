package com.example.odyssea.services;

import com.example.odyssea.daos.HotelDao;
import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelService {

    private final HotelDao hotelDao;

    @Autowired
    public HotelService(HotelDao hotelDao) {
        this.hotelDao = hotelDao;
    }

    /**
     * Récupère tous les hôtels disponibles dans la base de données
     */
    public List<Hotel> getAllHotels() {
        return hotelDao.findAll();
    }

    /**
     * Récupère un hôtel spécifique par son identifiant
     */
    public Hotel getHotel(int id) {
        return hotelDao.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Hotel not found with id: " + id));
    }

    /**
     * Crée un nouvel hôtel dans la base de données
     */
    public void createHotel(HotelDto hotelDto) {
        // Conversion du DTO en entité Hotel
        Hotel hotel = hotelDto.toEntity();

        // Vérifie que la ville existe avant de sauvegarder l'hôtel
        if (!hotelDao.cityExists(hotel.getCityId())) {
            throw new ResourceNotFoundException("City not found with id: " + hotel.getCityId());
        }
        hotelDao.save(hotel);
    }

    /**
     * Met à jour les informations d'un hôtel existant
     */
    public boolean updateHotel(int id, HotelDto hotelDto) {
        if (!hotelDao.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found with id: " + id);
        }
        Hotel hotel = hotelDto.toEntity();
        hotel.setId(id);
        hotelDao.update(hotel);
        return true;
    }

    /**
     * Supprime un hôtel de la base de données
     */
    public boolean deleteHotel(int id) {
        if (!hotelDao.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found with id: " + id);
        }
        hotelDao.deleteById(id);
        return true;
    }

    /**
     * Récupère tous les hôtels situés dans une ville spécifique
     */
    public List<Hotel> getHotelsByCityId(int cityId) {
        List<Hotel> hotels = hotelDao.findByCityId(cityId);
        if (hotels.isEmpty()) {
            throw new ResourceNotFoundException("No hotels found for city id: " + cityId);
        }
        return hotels;
    }

    /**
     * Récupère les hôtels d'une ville en fonction de leur standing (nombre d'étoiles)
     */
    public List<Hotel> getHotelsByCityAndStarRating(int cityId, int starRating) {
        List<Hotel> hotels = hotelDao.findByCityIdAndStarRating(cityId, starRating);
        if (hotels.isEmpty()) {
            throw new ResourceNotFoundException("No hotels found for city id: " + cityId + " with star rating: " + starRating);
        }
        return hotels;
    }
}
