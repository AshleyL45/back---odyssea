package com.example.odyssea.controllers.flight;

import com.example.odyssea.dtos.flight.FlightSegmentDTO;
import com.example.odyssea.services.flight.FlightSegmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flightSegments")
public class FlightSegmentController {

    private final FlightSegmentService flightSegmentService;

    public FlightSegmentController(FlightSegmentService flightSegmentService) {
        this.flightSegmentService = flightSegmentService;
    }

    @GetMapping
    public List<FlightSegmentDTO> getAll() {
        return flightSegmentService.findAll();
    }

    @GetMapping("/{id}")
    public FlightSegmentDTO getById(@PathVariable int id) {
        return flightSegmentService.findById(id);
    }

    @PostMapping
    public FlightSegmentDTO create(@RequestBody FlightSegmentDTO dto) {
        return flightSegmentService.save(dto);
    }

    @PutMapping("/{id}")
    public FlightSegmentDTO update(@PathVariable int id, @RequestBody FlightSegmentDTO dto) {
        return flightSegmentService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        flightSegmentService.delete(id);
    }
}
