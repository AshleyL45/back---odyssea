package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.mainTables.HotelDto;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.services.mainTables.HotelService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/{id}")
    public Mono<Hotel> getHotelById(@PathVariable int id) {
        return hotelService.getHotel(id);
    }

    @PostMapping
    public void createHotel(@RequestBody HotelDto hotelDto) {
        hotelService.createHotel(hotelDto);
    }

    @PutMapping("/{id}")
    public boolean updateHotel(@PathVariable int id, @RequestBody HotelDto hotelDto) {
        return hotelService.updateHotel(id, hotelDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteHotel(@PathVariable int id) {
        return hotelService.deleteHotel(id);
    }

    /**
     * Récupère les hôtels d'une ville spécifique avec un certain standing dans la BDD
     */
    @GetMapping("/by-city-and-star")
    public ResponseEntity<List<Hotel>> getHotelsByCityAndStar(
            @RequestParam int cityId,
            @RequestParam int starRating
    ) {
        List<Hotel> hotels = hotelService.getHotelsByCityAndStarRating(cityId, starRating);
        return ResponseEntity.ok(hotels);
    }

    /**
     * En un seul appel : récupère ou crée un hôtel pour une ville et un standing donné.
     * Si trouvé en BDD, retourné directement ; sinon, interroge Amadeus, crée 2 versions (4★/5★)
     * et retourne celle dont le standing est demandé.
     */
    @GetMapping("/from-amadeus/by-iata-and-save")
    public Mono<HotelDto> fetchAndSaveHotel(
            @RequestParam String iataCityCode,
            @RequestParam int cityId,
            @RequestParam int starRating
    ) {
        return hotelService.fetchAndSaveHotelWithStarFromAmadeusByCity(iataCityCode, cityId, starRating);
    }

}
