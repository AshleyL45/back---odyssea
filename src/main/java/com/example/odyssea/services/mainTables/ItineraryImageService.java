package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.ImageDao;
import com.example.odyssea.daos.mainTables.ItineraryImageDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItineraryImageService {
    private final ItineraryImageDao itineraryImageDao;
    private final ImageDao imageDao;

    public ItineraryImageService(ItineraryImageDao itineraryImageDao, ImageDao imageDao) {
        this.itineraryImageDao = itineraryImageDao;
        this.imageDao = imageDao;
    }

    /** Récupère les rôles pour un itinéraire (header, day1, etc.) */
    public List<String> listRoles(int itineraryId) {
        return itineraryImageDao.findRolesByItinerary(itineraryId);
    }

    /** Récupère les octets de l’image pour cet itinéraire & rôle */
    public byte[] getImageData(int itineraryId, String role) {
        return imageDao.findDataByItineraryAndRole(itineraryId, role);
    }
}
