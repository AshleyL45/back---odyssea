package com.example.odyssea.services;

import com.example.odyssea.daos.FlightSegmentDao;
import com.example.odyssea.dtos.Flight.FlightSegmentDTO;
import com.example.odyssea.entities.mainTables.FlightSegment;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightSegmentService {

    private final FlightSegmentDao flightSegmentDao;

    public FlightSegmentService(FlightSegmentDao flightSegmentDao) {
        this.flightSegmentDao = flightSegmentDao;
    }

    public List<FlightSegmentDTO> findAll() {
        return flightSegmentDao.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public FlightSegmentDTO findById(int id) {
        FlightSegment segment = flightSegmentDao.findById(id);
        return convertToDto(segment);
    }

    public FlightSegmentDTO save(FlightSegmentDTO dto) {
        FlightSegment segment = convertToEntity(dto);
        FlightSegment saved = flightSegmentDao.save(segment);
        return convertToDto(saved);
    }

    public FlightSegmentDTO update(int id, FlightSegmentDTO dto) {
        FlightSegment segment = convertToEntity(dto);
        FlightSegment updated = flightSegmentDao.update(id, segment);
        return convertToDto(updated);
    }

    public boolean delete(int id) {
        return flightSegmentDao.delete(id);
    }

    private FlightSegmentDTO convertToDto(FlightSegment segment) {
        FlightSegmentDTO dto = new FlightSegmentDTO();
        // Comme l'ID dans FlightSegmentDTO est de type String, on convertit l'entité en String.
        dto.setId(String.valueOf(segment.getId()));
        // Pour mapper la partie "departure", on crée un AirportDTO à partir du code
        // (ici on suppose que la date/heure n'est pas nécessaire pour la conversion du segment)
        dto.setDeparture(new com.example.odyssea.dtos.Flight.AirportDTO(segment.getDepartureAirportIata(), null));
        dto.setArrival(new com.example.odyssea.dtos.Flight.AirportDTO(segment.getArrivalAirportIata(), null));
        dto.setCarrierCode(segment.getCarrierCode());
        dto.setAircraftCode(new com.example.odyssea.dtos.Flight.AircraftDTO(segment.getAircraftCode()));
        // Pour la durée, on la convertit en String (vous pouvez ajuster le format si nécessaire)
        dto.setDuration(segment.getDuration().toString());
        return dto;
    }

    private FlightSegment convertToEntity(FlightSegmentDTO dto) {
        FlightSegment segment = new FlightSegment();
        // On convertit l'ID en entier (si le DTO possède un ID non nul)
        if(dto.getId() != null) {
            segment.setId(Integer.parseInt(dto.getId()));
        }
        // On récupère le code IATA depuis l'objet AirportDTO contenu dans le DTO
        segment.setDepartureAirportIata(dto.getDeparture() != null ? dto.getDeparture().getIataCode() : null);
        segment.setArrivalAirportIata(dto.getArrival() != null ? dto.getArrival().getIataCode() : null);
        segment.setCarrierCode(dto.getCarrierCode());
        // Pour le nom du transporteur et de l'avion, on peut laisser vide ou y ajouter une logique de conversion
        segment.setCarrierName("");
        segment.setAircraftCode(dto.getAircraftCode() != null ? dto.getAircraftCode().getCode() : null);
        segment.setAircraftName("");
        // Convertir la durée (en String) en objet Duration
        segment.setDuration(Duration.parse(dto.getDuration()));
        // Remarque : Si votre entité FlightSegment possède des champs pour la date/heure (ex : departureDateTime),
        // il faut les gérer ici. On suppose ici qu'ils sont déjà gérés par d'autres mécanismes.
        return segment;
    }
}
