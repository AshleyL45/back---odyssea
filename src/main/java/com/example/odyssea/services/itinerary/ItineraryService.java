package com.example.odyssea.services.itinerary;

import com.example.odyssea.daos.itinerary.ItineraryDao;
import com.example.odyssea.daos.itinerary.ItineraryStepDao;
import com.example.odyssea.daos.mainTables.ThemeDao;
import com.example.odyssea.dtos.mainTables.DailyPlanDto;
import com.example.odyssea.dtos.mainTables.DailyPlanWithCityDto;
import com.example.odyssea.dtos.mainTables.ItineraryDetails;
import com.example.odyssea.dtos.mainTables.ItineraryThemes;
import com.example.odyssea.entities.itinerary.Itinerary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItineraryService {

    private final ItineraryDao itineraryDao;
    private final ItineraryStepDao itineraryStepDao;
    private final ThemeDao themeDao;

    public ItineraryService(ItineraryDao itineraryDao, ItineraryStepDao itineraryStepDao, ThemeDao themeDao) {
        this.itineraryDao = itineraryDao;
        this.itineraryStepDao = itineraryStepDao;
        this.themeDao = themeDao;
    }


    public List<Itinerary> getAllItineraries() {
        return itineraryDao.findAll();
    }

    public List<ItineraryThemes> getAllItinerariesWithThemes(){
        return itineraryDao.findAllItinerariesWithTheme();
    }

    public Itinerary getItineraryById(int id) {
        return itineraryDao.findById(id);
    }

    public List<Itinerary> searchItineraries(String query){
        return itineraryDao.searchItinerary(query);
    }

    public ItineraryDetails getItineraryDetails(int id){
        Itinerary itinerary = itineraryDao.findById(id);
        List<DailyPlanDto> daysDetails = itineraryStepDao.findByItineraryId(itinerary.getId());
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

    private List<DailyPlanWithCityDto> getDailyPlanWithCity(int itineraryId) {
        return itineraryStepDao.findByItineraryIdWithCity(itineraryId);
    }

}