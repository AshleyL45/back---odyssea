package com.example.odyssea.services.flight;

import com.example.odyssea.daos.flight.FlightSegmentDao;
import com.example.odyssea.dtos.Flight.AirportDTO;
import com.example.odyssea.dtos.Flight.AircraftDTO;
import com.example.odyssea.dtos.Flight.FlightSegmentDTO;
import com.example.odyssea.entities.mainTables.FlightSegment;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
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
        FlightSegmentDTO result = convertToDto(saved);
        System.out.println("Registered segment, ID: " + result.getId());
        return result;
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
        // Récupérer les informations complètes des aéroports avec leur dateTime
        dto.setDeparture(new AirportDTO(segment.getDepartureAirportIata(), segment.getDepartureDateTime()));
        dto.setArrival(new AirportDTO(segment.getArrivalAirportIata(), segment.getArrivalDateTime()));
        dto.setCarrierCode(segment.getCarrierCode());
        dto.setAircraftCode(new AircraftDTO(segment.getAircraftCode()));
        dto.setDuration(segment.getDuration().toString());
        return dto;
    }

    private FlightSegment convertToEntity(FlightSegmentDTO dto) {
        FlightSegment segment = new FlightSegment();

        // Conversion de l'ID, si présent
        if(dto.getId() != null && !dto.getId().isEmpty()){
            try {
                segment.setId(Integer.parseInt(dto.getId()));
            } catch (NumberFormatException e){
                System.err.println("ID conversion error: " + e.getMessage());
            }
        }

        // Récupération des informations de départ
        if(dto.getDeparture() != null){
            segment.setDepartureAirportIata(dto.getDeparture().getIataCode());
            segment.setDepartureDateTime(dto.getDeparture().getDateTime());
        } else {
            segment.setDepartureAirportIata(null);
            segment.setDepartureDateTime(null);
        }

        // Récupération des informations d'arrivée
        if(dto.getArrival() != null){
            segment.setArrivalAirportIata(dto.getArrival().getIataCode());
            segment.setArrivalDateTime(dto.getArrival().getDateTime());
        } else {
            segment.setArrivalAirportIata(null);
            segment.setArrivalDateTime(null);
        }

        // Transporteur
        segment.setCarrierCode(dto.getCarrierCode());
        // Utilisation du carrierName enrichi dans le DTO
        segment.setCarrierName(dto.getCarrierName());

        // Avion
        if(dto.getAircraftCode() != null){
            segment.setAircraftCode(dto.getAircraftCode().getCode());
        } else {
            segment.setAircraftCode(null);
        }
        // Utilisation du aircraftName enrichi dans le DTO
        segment.setAircraftName(dto.getAircraftName());


        // Conversion de la durée
        String durationStr = dto.getDuration();
        Duration duration;
        try {
            duration = Duration.parse(durationStr);
        } catch (Exception e){
            try{
                LocalTime lt = LocalTime.parse(durationStr);
                duration = Duration.ofHours(lt.getHour())
                        .plusMinutes(lt.getMinute())
                        .plusSeconds(lt.getSecond());
            } catch(Exception ex){
                System.err.println("Time conversion error: " + ex.getMessage());
                duration = Duration.ZERO;
            }
        }
        segment.setDuration(duration);

        return segment;
    }

}
