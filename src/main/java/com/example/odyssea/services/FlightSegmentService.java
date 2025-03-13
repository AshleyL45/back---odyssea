package com.example.odyssea.services;

import com.example.odyssea.daos.FlightSegmentDao;
import com.example.odyssea.dtos.Flight.FlightDTO;
import com.example.odyssea.dtos.Flight.FlightDataDTO;
import com.example.odyssea.dtos.Flight.FlightSegmentDTO;
import com.example.odyssea.entities.mainTables.FlightSegment;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class FlightSegmentService {
    private final FlightSegmentDao flightSegmentDao;

    public FlightSegmentService(FlightSegmentDao flightSegmentDao) {
        this.flightSegmentDao = flightSegmentDao;
    }

    public FlightSegment flightSegmentDTOtoFlightSegment(FlightDataDTO flightDataDTO, FlightSegmentDTO flightSegmentDTO){
        LocalTime time = LocalTime.parse(flightSegmentDTO.getDuration());
        return new FlightSegment(
                flightSegmentDTO.getDeparture().getIataCode(),
                flightSegmentDTO.getArrival().getIataCode(),
                flightSegmentDTO.getDeparture().getDateTime(),
                flightSegmentDTO.getArrival().getDateTime(),
                flightSegmentDTO.getCarrierCode(),
                flightDataDTO.getDictionnary().getCarriers().get(flightSegmentDTO.getCarrierCode()),
                flightSegmentDTO.getAircraftCode().getCode(),
                flightDataDTO.getDictionnary().getAircraft().get(flightSegmentDTO.getAircraftCode()),
                time
        );
    }
}
