package com.example.odyssea.services;

import com.example.odyssea.daos.MySelectionDao;
import com.example.odyssea.entities.MySelection;
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
                .filter(selection -> selection.getIdUser() == userId)
                .collect(Collectors.toList());
    }

    // Retourne les sélections pour un itinéraire donné
    public List<MySelection> getSelectionsByItineraryId(int itineraryId) {
        return mySelectionDao.findAll().stream()
                .filter(selection -> selection.getIdItinerary() == itineraryId)
                .collect(Collectors.toList());
    }

    // Retourne la sélection correspondant à un utilisateur et un itinéraire donnés
    public MySelection getSelection(int userId, int itineraryId) {
        return mySelectionDao.findAll().stream()
                .filter(selection -> selection.getIdUser() == userId && selection.getIdItinerary() == itineraryId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Selection with userId: " + userId + " and itineraryId: " + itineraryId + " does not exist"));
    }

    // Crée une nouvelle sélection
    public MySelection createSelection(MySelection selection) {
        return mySelectionDao.save(selection);
    }

    // Met à jour une sélection pour un utilisateur donné
    public MySelection updateSelection(int userId, MySelection selection) {

        return mySelectionDao.update(userId, selection);
    }

    // Supprime une sélection correspondant à un utilisateur et un itinéraire donnés
    public boolean deleteSelection(int userId, int itineraryId) {
        List<MySelection> selections = mySelectionDao.findAll();
        boolean deleted = false;
        for (MySelection s : selections) {
            if (s.getIdUser() == userId && s.getIdItinerary() == itineraryId) {
                deleted = mySelectionDao.delete(itineraryId);
                break;
            }
        }
        return deleted;
    }
}
