package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.ItineraryImageDao;
import com.example.odyssea.daos.mainTables.ImageDao;
import com.example.odyssea.exceptions.ImageNotFoundException;
import com.example.odyssea.exceptions.ImageProcessingException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    private final ItineraryImageDao itineraryImageDao;
    private final ImageDao imageDao;

    public ImageService(ItineraryImageDao itineraryImageDao,
                        ImageDao imageDao) {
        this.itineraryImageDao = itineraryImageDao;
        this.imageDao = imageDao;
    }

    public List<String> listImageRoles(int itineraryId) {
        try {
            List<String> roles = itineraryImageDao.findRolesByItinerary(itineraryId);
            if (roles == null || roles.isEmpty()) {
                throw new ImageNotFoundException(
                        "No image roles found for itinerary ID=" + itineraryId
                );
            }
            return roles;
        } catch (DataAccessException ex) {
            throw new ImageProcessingException(
                    "Error retrieving image roles for itinerary ID=" + itineraryId, ex
            );
        }
    }

    public byte[] getImageData(int itineraryId, String role) {
        try {
            byte[] data = imageDao.findDataByItineraryAndRole(itineraryId, role);
            if (data == null || data.length == 0) {
                throw new ImageNotFoundException(
                        String.format("No image found for itinerary ID=%d and role='%s'", itineraryId, role)
                );
            }
            return data;
        } catch (DataAccessException ex) {
            throw new ImageProcessingException(
                    String.format("Error retrieving image data for itinerary ID=%d and role='%s'", itineraryId, role),
                    ex
            );
        }
    }
}
