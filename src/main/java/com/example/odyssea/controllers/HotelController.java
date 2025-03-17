package com.example.odyssea.controllers;

import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.services.HotelService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * Récupère tous les hôtels disponibles (mode bloquant)
     */
    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    /**
     * Récupère un hôtel spécifique par son identifiant de manière asynchrone.
     */
    @GetMapping("/{id}")
    public Mono<Hotel> getHotelById(@PathVariable int id) {
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
     * Récupère les hôtels d'une ville spécifique avec un certain standing (nombre d'étoiles)
     */
    @GetMapping("/from-amadeus/by-iata-and-save")
    public Mono<List<HotelDto>> fetchAndSaveHotelsByIataAndStar(@RequestParam String iataCityCode,
                                                                @RequestParam int starRating) {
        System.out.println("Requête fetchAndSaveHotelsByIataAndStar reçue avec iataCityCode: "
                + iataCityCode + ", starRating: " + starRating);
        return hotelService.fetchAndSaveHotelsFromAmadeus(iataCityCode, starRating);
    }


}
