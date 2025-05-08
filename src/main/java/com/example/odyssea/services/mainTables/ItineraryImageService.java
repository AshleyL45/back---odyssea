// src/main/java/com/example/odyssea/services/mainTables/ItineraryImageService.java
package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.ItineraryImageDao;
import com.example.odyssea.daos.mainTables.ImageDao;
import com.example.odyssea.dtos.mainTables.ItineraryImageDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItineraryImageService {
    private final ItineraryImageDao itineraryImageDao;
    private final ImageDao imageDao;

    public ItineraryImageService(ItineraryImageDao itineraryImageDao,
                                 ImageDao imageDao) {
        this.itineraryImageDao = itineraryImageDao;
        this.imageDao = imageDao;
    }

    /** Pour l’ancien ItineraryController */
    public List<ItineraryImageDto> getImagesForItinerary(int itineraryId) {
        return itineraryImageDao.findByItineraryId(itineraryId);
    }

    /** Pour lister les rôles dans ItineraryImageController */
    public List<String> listRoles(int itineraryId) {
        return itineraryImageDao.findRolesByItinerary(itineraryId);
    }

    /** Pour renvoyer le blob selon le rôle */
    public byte[] getImageData(int itineraryId, String role) {
        // on peut aussi directement interroger avec un JOIN dans ImageDao
        return imageDao.findDataByItineraryAndRole(itineraryId, role);
    }
}
