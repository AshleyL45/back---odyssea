package com.example.odyssea.services.flight;

import com.example.odyssea.daos.flight.FlightSegmentDao;
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

        dto.setId(String.valueOf(segment.getId()));
        dto.setDeparture(new com.example.odyssea.dtos.Flight.AirportDTO(segment.getDepartureAirportIata(), null));
        dto.setArrival(new com.example.odyssea.dtos.Flight.AirportDTO(segment.getArrivalAirportIata(), null));
        dto.setCarrierCode(segment.getCarrierCode());
        dto.setAircraftCode(new com.example.odyssea.dtos.Flight.AircraftDTO(segment.getAircraftCode()));
        dto.setDuration(segment.getDuration().toString());
        return dto;
    }

    private FlightSegment convertToEntity(FlightSegmentDTO dto) {
        FlightSegment segment = new FlightSegment();

        if(dto.getId() != null) {
            segment.setId(Integer.parseInt(dto.getId()));
        }
        // On récupère le code IATA depuis l'objet AirportDTO contenu dans le DTO
        segment.setDepartureAirportIata(dto.getDeparture() != null ? dto.getDeparture().getIataCode() : null);
        segment.setArrivalAirportIata(dto.getArrival() != null ? dto.getArrival().getIataCode() : null);
        segment.setCarrierCode(dto.getCarrierCode());
        segment.setCarrierName("");
        segment.setAircraftCode(dto.getAircraftCode() != null ? dto.getAircraftCode().getCode() : null);
        segment.setAircraftName("");
        segment.setDuration(Duration.parse(dto.getDuration()));
        return segment;
    }
}
