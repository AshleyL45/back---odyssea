package com.example.odyssea.services.userItinerary;

import com.example.odyssea.daos.itinerary.ItineraryDao;
import com.example.odyssea.daos.userItinerary.InteractiveMapRepository;
import com.example.odyssea.dtos.userItinerary.InteractiveMapDto;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractiveMapService {

   private final CurrentUserService currentUserService;
   private final InteractiveMapRepository interactiveMapRepository;

    public InteractiveMapService(CurrentUserService currentUserService, InteractiveMapRepository interactiveMapRepository) {
        this.currentUserService = currentUserService;
        this.interactiveMapRepository = interactiveMapRepository;
    }

    public List<InteractiveMapDto> getItinerariesForUser(int itineraryId){
        Integer userId = currentUserService.getCurrentUserId();
        return interactiveMapRepository.getItineraryForUser(userId, itineraryId);
    }
}