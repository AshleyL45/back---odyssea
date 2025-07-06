package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.ImageDao;
import com.example.odyssea.entities.mainTables.Image;
import com.example.odyssea.exceptions.ImageNotFoundException;
import com.example.odyssea.exceptions.ImageProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private final ImageDao imageDao;

    public ImageService(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    public byte[] getImageData(int itineraryId, String role) {
        Image img;
        try {
            img = imageDao.findByItineraryAndRole(itineraryId, role);
        } catch (DataAccessException ex) {
            logger.error("Database access error for itinerary {} and role {}", itineraryId, role, ex);
            throw new ImageProcessingException(
                    "Error accessing database for itinerary " + itineraryId + ", role '" + role + "'", ex
            );
        }

        if (img == null) {
            throw new ImageNotFoundException(
                    "No image in database for itinerary " + itineraryId + ", role '" + role + "'"
            );
        }

        String link = img.getLink();
        String sanitized = link.startsWith("/") ? link.substring(1) : link;
        String resourcePath;

        Matcher matcher = Pattern.compile("images/itineraries/(\\d+)/(.*)").matcher(sanitized);
        if (matcher.matches()) {
            String id = matcher.group(1);
            String file = matcher.group(2);
            resourcePath = String.format("static/images/itineraries/itinerary%s/%s", id, file);
        } else {
            resourcePath = "static/" + sanitized;
        }

        ClassPathResource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new ImageNotFoundException("File not found: " + resourcePath);
        }

        try (InputStream is = resource.getInputStream()) {
            byte[] data = StreamUtils.copyToByteArray(is);
            if (data.length == 0) {
                throw new ImageNotFoundException("Empty file: " + resourcePath);
            }
            return data;
        } catch (IOException ex) {
            logger.error("Error reading file {}", resourcePath, ex);
            throw new ImageProcessingException(
                    "Error reading file '" + resourcePath + "'", ex
            );
        }
    }

    public List<String> listImageRoles(int itineraryId) {
        try {
            return imageDao.findRolesByItinerary(itineraryId);
        } catch (DataAccessException ex) {
            logger.error("Database access error while retrieving roles for itinerary {}", itineraryId, ex);
            throw new ImageProcessingException(
                    "Error accessing database while retrieving roles for itinerary " + itineraryId, ex
            );
        }
    }
}
