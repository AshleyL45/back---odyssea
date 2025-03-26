package com.example.odyssea.services.itinerary;

import com.example.odyssea.daos.itinerary.ItineraryStepDao;
import com.example.odyssea.dtos.mainTables.DailyPlanDto;
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
