package com.example.odyssea.services;

import com.example.odyssea.daos.ItineraryActivityPerDayDao;
import com.example.odyssea.entities.itinerary.ItineraryActivityPerDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItineraryActivityPerDayService {

    private final ItineraryActivityPerDayDao itineraryActivityPerDayDao;

    @Autowired
    public ItineraryActivityPerDayService(ItineraryActivityPerDayDao itineraryActivityPerDayDao) {
        this.itineraryActivityPerDayDao = itineraryActivityPerDayDao;
    }

    /**
     * Récupère toutes les associations activité/jour pour tous les itinéraires
     */
    public List<ItineraryActivityPerDay> getAllActivitiesPerDay() {
        return itineraryActivityPerDayDao.findAll();
    }

    /**
     * Récupère une activité spécifique pour un jour donné dans une étape d'itinéraire
     */
    public Optional<ItineraryActivityPerDay> getActivityPerDay(int itineraryStepId, int activityId, int dayNumber) {
        return itineraryActivityPerDayDao.findByCompositeKey(itineraryStepId, activityId, dayNumber);
    }

    /**
     * Crée une nouvelle association activité/jour pour un itinéraire
     */
    public void createActivityPerDay(ItineraryActivityPerDay entity) {
        itineraryActivityPerDayDao.save(entity);
    }

    /**
     * Supprime une activité spécifique pour un jour donné dans une étape d'itinéraire
     */
    public void deleteActivityPerDay(int itineraryStepId, int activityId, int dayNumber) {
        itineraryActivityPerDayDao.delete(itineraryStepId, activityId, dayNumber);
    }
}
