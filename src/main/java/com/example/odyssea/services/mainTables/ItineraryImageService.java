package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.ImageDao;
import com.example.odyssea.daos.mainTables.ItineraryImageDao;
import com.example.odyssea.entities.mainTables.Image;
import com.example.odyssea.exceptions.ImageNotFoundException;
import com.example.odyssea.exceptions.ImageProcessingException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ItineraryImageService {
    private final ItineraryImageDao itineraryImageDao;
    private final ImageDao imageDao;

    public ItineraryImageService(ItineraryImageDao itineraryImageDao, ImageDao imageDao) {
        this.itineraryImageDao = itineraryImageDao;
        this.imageDao = imageDao;
    }

    public List<String> listRoles(int itineraryId) {
        List<String> roles = itineraryImageDao.findRolesByItinerary(itineraryId);
        if (roles == null || roles.isEmpty()) {
            throw new ImageNotFoundException(
                    String.format("Aucune image enregistrée pour l'itinéraire %d", itineraryId)
            );
        }
        return roles;
    }

    public byte[] getImageData(int itineraryId, String role) {
        List<String> roles = listRoles(itineraryId);
        if (!roles.contains(role)) {
            throw new ImageNotFoundException(
                    String.format("Le rôle '%s' n'existe pas pour l'itinéraire %d", role, itineraryId)
            );
        }

        Image img = imageDao.findByItineraryAndRole(itineraryId, role);
        if (img == null) {
            throw new ImageNotFoundException(
                    String.format("Aucune image trouvée pour l'itinéraire %d et le rôle '%s'", itineraryId, role)
            );
        }

        String link = img.getLink();
        String sanitized = link.startsWith("/") ? link.substring(1) : link;

        Matcher m = Pattern.compile("images/itineraries/(\\d+)/(.*)").matcher(sanitized);
        String resourcePath;
        if (m.matches()) {
            resourcePath = String.format(
                    "static/images/itineraries/itinerary%s/%s",
                    m.group(1), m.group(2)
            );
        } else {
            resourcePath = "static/" + sanitized;
        }

        try {
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
            }
        } catch (IOException ex) {
            throw new ImageProcessingException(
                    String.format("Erreur lecture du fichier '%s'", resourcePath), ex
            );
        }
    }
}
