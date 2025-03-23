package com.example.odyssea.services;

import com.example.odyssea.daos.ItineraryDao;
import com.example.odyssea.daos.ItineraryStepDao;
import com.example.odyssea.daos.ThemeDao;
import com.example.odyssea.dtos.DailyPlanDto;
import com.example.odyssea.dtos.ItineraryResponseDTO;
import com.example.odyssea.dtos.ItineraryThemes;
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

    public ItineraryResponseDTO getItineraryDetails(int id){
        Itinerary itinerary = itineraryDao.findById(id);
        List<DailyPlanDto> daysDetails = itineraryStepDao.findByItineraryId(itinerary.getId());
        return new ItineraryResponseDTO(
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

    public Itinerary createItinerary(Itinerary itinerary) {
        return itineraryDao.save(itinerary);
    }

    public Itinerary updateItinerary(int id, Itinerary itinerary) {
        return itineraryDao.update(id, itinerary);
    }

    public boolean deleteItinerary(int id) {
        return itineraryDao.delete(id);
    }
}