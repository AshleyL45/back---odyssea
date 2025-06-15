package com.example.odyssea.services.itinerary;

import com.example.odyssea.daos.itinerary.ItineraryDao;
import com.example.odyssea.daos.itinerary.ItineraryStepDao;
import com.example.odyssea.daos.mainTables.ThemeDao;
import com.example.odyssea.dtos.mainTables.DailyPlanDto;
import com.example.odyssea.dtos.mainTables.DailyPlanWithCityDto;
import com.example.odyssea.dtos.mainTables.ItineraryDetails;
import com.example.odyssea.dtos.mainTables.ItinerarySummary;
import com.example.odyssea.entities.itinerary.Itinerary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItineraryService {

    private final ItineraryDao itineraryDao;
    private final ItineraryStepService itineraryStepService;
    private final ItineraryStepDao itineraryStepDao;
    private final ThemeDao themeDao;

    public ItineraryService(ItineraryDao itineraryDao, ItineraryStepService itineraryStepService, ItineraryStepDao itineraryStepDao, ThemeDao themeDao) {
        this.itineraryDao = itineraryDao;
        this.itineraryStepService = itineraryStepService;
        this.itineraryStepDao = itineraryStepDao;
        this.themeDao = themeDao;
    }


    public List<Itinerary> getAllItineraries() {
        return itineraryDao.findAll();
    }

    public List<ItinerarySummary> getAllItinerariesSummaries(){
        return itineraryDao.findAllItinerariesSummaries();
    }

    public List<Itinerary> searchItineraries(String query){
        return itineraryDao.searchItinerary(query);
    }

    public ItineraryDetails getItineraryDetails(int id){
        Itinerary itinerary = itineraryDao.findById(id);
        List<DailyPlanDto> daysDetails = itineraryStepService.getDailyPlansByItineraryId(itinerary.getId());
        return new ItineraryDetails(
                itinerary.getId(),
                itinerary.getName(),
                itinerary.getDescription(),
                itinerary.getShortDescription(),
                itinerary.getStock(),
                itinerary.getPrice(),
                itinerary.getTotalDuration(),
                themeDao.findById(itinerary.getThemeId()).getThemeName(),
                daysDetails
        );
    }

    private List<DailyPlanWithCityDto> getDailyPlanWithCity(int itineraryId) {//TODO Demander à Ashley
        return itineraryStepDao.findByItineraryIdWithCity(itineraryId);
    }

    public List<Itinerary> findValidItineraries(List<String> excludedCountries) {
        return itineraryDao.findValidItineraries(excludedCountries);
    }

}