package com.example.odyssea.services;

import com.example.odyssea.daos.MySelectionDao;
import com.example.odyssea.entities.MySelection;
import com.example.odyssea.entities.itinerary.Itinerary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MySelectionService {

    private final MySelectionDao mySelectionDao;

    public MySelectionService(MySelectionDao mySelectionDao) {
        this.mySelectionDao = mySelectionDao;
    }

    // Retourne toutes les sélections
    public List<MySelection> getAllSelections() {
        return mySelectionDao.findAll();
    }

    // Retourne les sélections pour un utilisateur donné
    public List<MySelection> getSelectionsByUserId(int userId) {
        return mySelectionDao.findAll().stream()
                .filter(selection -> selection.getUserId() == userId)
                .collect(Collectors.toList());
    }

    // Retourne les sélections d'un utilisateur sous forme de liste d'itinéraires
    public List<Itinerary> getUserFavorites(int userId){
        return mySelectionDao.findAllUserFavorites(userId);
    }

    // Retourne les sélections pour un itinéraire donné
    public List<MySelection> getSelectionsByItineraryId(int itineraryId) {
        return mySelectionDao.findAll().stream()
                .filter(selection -> selection.getItineraryId() == itineraryId)
                .collect(Collectors.toList());
    }

    // Retourne la sélection correspondant à un utilisateur et un itinéraire donnés
    public MySelection getSelection(int userId, int itineraryId) {
        return mySelectionDao.findAll().stream()
                .filter(selection -> selection.getUserId() == userId && selection.getItineraryId() == itineraryId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Selection with userId: " + userId + " and itineraryId: " + itineraryId + " does not exist"));
    }

    // Crée une nouvelle sélection
    public MySelection createSelection(MySelection selection) {
        return mySelectionDao.save(selection);
    }

    // Met à jour une sélection pour un utilisateur donné
    public MySelection updateSelection(int userId, int itineraryId, MySelection selection) {
        return mySelectionDao.update(userId, itineraryId, selection);
    }

    // Supprime une sélection correspondant à un utilisateur et un itinéraire donnés
    public boolean deleteSelection(int userId, int itineraryId) {
        List<MySelection> selections = mySelectionDao.findAll();
        boolean deleted = false;
        for (MySelection s : selections) {
            if (s.getUserId() == userId && s.getItineraryId() == itineraryId) {
                deleted = mySelectionDao.delete(userId, itineraryId);
                break;
            }
        }
        return deleted;
    }
}
