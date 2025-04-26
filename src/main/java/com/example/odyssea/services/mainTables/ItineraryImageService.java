package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.ItineraryImageDao;
import com.example.odyssea.dtos.mainTables.ItineraryImageDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItineraryImageService {

    private final ItineraryImageDao dao;

    public ItineraryImageService(ItineraryImageDao dao) {
        this.dao = dao;
    }

    public List<ItineraryImageDto> getImagesForItinerary(int itineraryId) {
        return dao.findByItineraryId(itineraryId);
    }
}


