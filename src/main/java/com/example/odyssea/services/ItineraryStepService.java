package com.example.odyssea.services;

import com.example.odyssea.daos.ItineraryStepDao;
import com.example.odyssea.dtos.DailyPlanDto;
import com.example.odyssea.entities.itinerary.ItineraryStep;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItineraryStepService {
    private final ItineraryStepDao itineraryStepDao;

    public ItineraryStepService(ItineraryStepDao itineraryStepDao) {
        this.itineraryStepDao = itineraryStepDao;
    }

    public List<DailyPlanDto> getDailyPlansByItineraryId(int itineraryId){
        return itineraryStepDao.findByItineraryId(itineraryId);
    }
}
