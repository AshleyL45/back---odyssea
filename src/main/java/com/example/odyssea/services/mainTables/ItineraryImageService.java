package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.ItineraryImageDao;
import com.example.odyssea.daos.mainTables.ImageDao;
import com.example.odyssea.exceptions.ImageNotFoundException;
import com.example.odyssea.exceptions.ImageProcessingException;
import org.springframework.dao.DataAccessException;
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

    public List<String> listRoles(int itineraryId) {
        try {
            List<String> roles = itineraryImageDao.findRolesByItinerary(itineraryId);
            if (roles == null || roles.isEmpty()) {
                throw new ImageNotFoundException("No images recorded for this itinerary");
            }
            return roles;
        } catch (DataAccessException ex) {
            throw new ImageProcessingException("Unable to retrieve image roles", ex);
        }
    }

    public byte[] getImageData(int itineraryId, String role) {
        try {
            List<String> roles = itineraryImageDao.findRolesByItinerary(itineraryId);
            if (roles == null || roles.isEmpty()) {
                throw new ImageNotFoundException("No images recorded for this itinerary");
            }
            if (!roles.contains(role)) {
                throw new ImageNotFoundException("Requested image role not found");
            }
            byte[] data = imageDao.findDataByItineraryAndRole(itineraryId, role);
            if (data == null || data.length == 0) {
                throw new ImageNotFoundException("No image data available");
            }
            return data;
        } catch (DataAccessException ex) {
            throw new ImageProcessingException("Unable to retrieve image data", ex);
        }
    }
}
