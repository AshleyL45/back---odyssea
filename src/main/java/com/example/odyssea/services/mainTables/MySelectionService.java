package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.MySelectionDao;
import com.example.odyssea.entities.MySelection;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.expression.spel.ast.Selection;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MySelectionService {

    private final MySelectionDao mySelectionDao;
    private final CurrentUserService currentUserService;

    public MySelectionService(MySelectionDao mySelectionDao, CurrentUserService currentUserService) {
        this.mySelectionDao = mySelectionDao;
        this.currentUserService = currentUserService;
    }

    private Integer getUserId() {
        return currentUserService.getCurrentUserId();
    }

    // Retourne toutes les sélections
    public List<MySelection> getAllSelections() {
        return mySelectionDao.findAll();
    }

    // Retourne les sélections pour un utilisateur donné
    public List<MySelection> getSelectionsByUserId() {
        Integer userId = getUserId();
        return mySelectionDao.findAll().stream()
                .filter(selection -> selection.getUserId() == userId)
                .collect(Collectors.toList());
    }

    // Retourne les sélections d'un utilisateur sous forme de liste d'itinéraires
    public List<Itinerary> getUserFavorites(){
        Integer userId = getUserId();
        return mySelectionDao.findAllUserFavorites(userId);
    }

    // Retourne les sélections pour un itinéraire donné
    public List<MySelection> getSelectionsByItineraryId(int itineraryId) {
        return mySelectionDao.findAll().stream()
                .filter(selection -> selection.getItineraryId() == itineraryId)
                .collect(Collectors.toList());
    }

    // Retourne la sélection correspondant à un utilisateur et un itinéraire donnés
    public MySelection getSelection(int itineraryId) {
        Integer userId = getUserId();
        return mySelectionDao.findAll().stream()
                .filter(selection -> selection.getUserId() == userId && selection.getItineraryId() == itineraryId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Selection with userId: " + userId + " and itineraryId: " + itineraryId + " does not exist"));
    }

    // Crée une nouvelle sélection
    public void addToSelection(Integer itineraryId) {
        Integer userId = getUserId();
        MySelection selection = new MySelection(userId, itineraryId);
        mySelectionDao.save(selection);
    }

    // Met à jour une sélection pour un utilisateur donné
    public MySelection updateSelection(int itineraryId, MySelection selection) {
        Integer userId = getUserId();
        return mySelectionDao.update(userId, itineraryId, selection);
    }

    // Supprime une sélection correspondant à un utilisateur et un itinéraire donnés
    public void deleteFromSelection(int itineraryId) {
        Integer userId = getUserId();
        mySelectionDao.delete(userId, itineraryId);;
    }
}
