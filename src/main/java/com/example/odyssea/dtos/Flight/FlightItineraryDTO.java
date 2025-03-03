package com.example.odyssea.dtos.Flight;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

public class FlightItineraryDTO {

    @JsonProperty("segments")
    private List<FlightSegmentDTO> segments; // Liste de segments de vol
    private Duration duration;

    public FlightItineraryDTO() {
    }

    public FlightItineraryDTO(List<FlightSegmentDTO> segments, Duration duration) {
        this.segments = segments;
        this.duration = duration;
    }

    public List<FlightSegmentDTO> getSegments() {
        return segments;
    }

    public void setSegments(List<FlightSegmentDTO> segments) {
        this.segments = segments;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
