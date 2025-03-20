package com.example.odyssea.services;

import com.example.odyssea.daos.HotelDao;
import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class HotelService {

    private final HotelDao hotelDao;
    private final TokenService tokenService;
    private final WebClient webClient;

    public HotelService(HotelDao hotelDao, TokenService tokenService, WebClient.Builder webClientBuilder) {
        this.hotelDao = hotelDao;
        this.tokenService = tokenService;
        // Configuration de base du WebClient pour l'API Amadeus
        this.webClient = webClientBuilder.baseUrl("https://api.amadeus.com").build();
    }

    /**
     * Récupère tous les hôtels disponibles dans la base de données
     */
    public List<Hotel> getAllHotels() {
        return hotelDao.findAll();
    }

    /**
     * Récupère un hôtel spécifique par son identifiant de manière asynchrone.
     * Si l'hôtel n'existe pas en BDD, il est récupéré depuis l'API Amadeus, enregistré en BDD, puis retourné
     */
    public Mono<Hotel> getHotel(int id) {
        // Vérification en base de données
        return Mono.fromCallable(() -> hotelDao.findById(id))
                // // Encapsule une opération bloquante dans un Mono et l'exécute sur un thread dédié (boundedElastic) pour éviter de bloquer le thread principal
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optional -> {
                    if (optional.isPresent()) {
                        return Mono.just(optional.get());
                    } else {
                        // L'hôtel n'existe pas : on le récupère depuis l'API Amadeus
                        return fetchHotelDataFromAmadeus(String.valueOf(id))
                                .flatMap(dto ->
                                        // On enregistre l'hôtel en base dans un contexte bloquant dédié
                                        Mono.fromCallable(() -> {
                                            if (!hotelDao.cityExists(dto.toEntity().getCityId())) {
                                                throw new ResourceNotFoundException("City with id " + dto.toEntity().getCityId() + " not found.");
                                            }
                                            hotelDao.save(dto.toEntity());
                                            return dto.toEntity();
                                        }).subscribeOn(Schedulers.boundedElastic())
                                );
                    }
                });
    }

    /**
     * Crée un nouvel hôtel dans la base de données
     */
    public void createHotel(HotelDto hotelDto) {
        Hotel hotel = hotelDto.toEntity();
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
        try  {
            List<Hotel> hotels = hotelDao.findByCityIdAndStarRating(cityId, starRating);
            if (hotels.isEmpty()) {
                throw new ResourceNotFoundException("No hotels found for city id: " + cityId
                        + " with star rating: " + starRating);
            }
            return hotels;
        } catch (ResourceNotFoundException e){
            return null;
        }

    }

    /**
     * Récupère les données d'un hôtel depuis l'API Amadeus en utilisant un token valide
     */
    public Mono<HotelDto> fetchHotelDataFromAmadeus(String amadeusHotelId) {
        return tokenService.getValidToken().flatMap(token ->
                webClient.get()
                        .uri("/v1/hotels/" + amadeusHotelId)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(HotelDto.class)
        );
    }

    /**
     * Récupère les données d'un hôtel depuis l'API Amadeus et le crée dans la base de données
     */
    public Mono<Void> createHotelFromAmadeus(String amadeusHotelId) {
        return fetchHotelDataFromAmadeus(amadeusHotelId)
                .doOnNext(hotelDto -> {
                    createHotel(hotelDto);
                })
                .then();
    }
}
