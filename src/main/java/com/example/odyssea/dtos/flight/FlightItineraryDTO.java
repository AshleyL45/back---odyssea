package com.example.odyssea.dtos.flight;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.util.List;

public class FlightItineraryDTO {
    private Integer id;

    @JsonProperty("segments")
    private List<FlightSegmentDTO> segments;
    private Duration duration;

    public FlightItineraryDTO() {
    }

    public FlightItineraryDTO(Integer id, List<FlightSegmentDTO> segments, Duration duration) {
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
