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

    public List<ItineraryImageDto> getImagesForItinerary(int itineraryId) {
        return itineraryImageDao.findByItineraryId(itineraryId);
    }

    public List<String> listRoles(int itineraryId) {
        return itineraryImageDao.findRolesByItinerary(itineraryId);
    }

    public byte[] getImageData(int itineraryId, String role) {
        return imageDao.findDataByItineraryAndRole(itineraryId, role);
    }
}
