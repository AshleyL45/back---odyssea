package com.example.odyssea.controllers;

import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * Récupère tous les hôtels disponibles
     */
    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    /**
     * Récupère un hôtel spécifique par son identifiant
     */
    @GetMapping("/{id}")
    public Hotel getHotelById(@PathVariable int id) {
        return hotelService.getHotel(id);
    }

    /**
     * Crée un nouvel hôtel en utilisant les données fournies via un DTO
     */
    @PostMapping
    public void createHotel(@RequestBody HotelDto hotelDto) {
        hotelService.createHotel(hotelDto);
    }

    /**
     * Met à jour les informations d'un hôtel existant
     */
    @PutMapping("/{id}")
    public boolean updateHotel(@PathVariable int id, @RequestBody HotelDto hotelDto) {
        return hotelService.updateHotel(id, hotelDto);
    }

    /**
     * Supprime un hôtel par son identifiant
     */
    @DeleteMapping("/{id}")
    public boolean deleteHotel(@PathVariable int id) {
        return hotelService.deleteHotel(id);
    }

    /**
     * Récupère tous les hôtels situés dans une ville spécifique
     */
    @GetMapping("/by-city")
    public List<Hotel> getHotelsByCity(@RequestParam int cityId) {
        return hotelService.getHotelsByCityId(cityId);
    }

    /**
     * Récupère les hôtels d'une ville spécifique avec un certain standing (nombre d'étoiles)
     */
    @GetMapping("/by-city-and-star")
    public List<Hotel> getHotelsByCityAndStar(@RequestParam int cityId, @RequestParam int starRating) {
        return hotelService.getHotelsByCityAndStarRating(cityId, starRating);
    }
}
