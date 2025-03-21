package com.example.odyssea.services;

import com.example.odyssea.daos.ItineraryDao;
import com.example.odyssea.entities.itinerary.Itinerary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractiveMapService {

    private final ItineraryDao itineraryDao;

    public InteractiveMapService(ItineraryDao itineraryDao) {
        this.itineraryDao = itineraryDao;
    }

    public List<Itinerary> getAllItineraries() {
        return itineraryDao.findAll();
    }

    public Itinerary getItineraryById(int id) {
        return itineraryDao.findById(id);
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