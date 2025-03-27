package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.Min;

public class FlightSegmentRide {

    @Min(value = 1, message = "Plane Ride ID must be greater than or equal to 1")
    private int planeRideId;

    @Min(value = 1, message = "Flight Segment ID must be greater than or equal to 1")
    private int flightSegmentId;

    public FlightSegmentRide() {}

    public FlightSegmentRide(int planeRideId, int flightSegmentId) {
        this.planeRideId = planeRideId;
        this.flightSegmentId = flightSegmentId;
    }

    // Getters and setters
    public int getPlaneRideId() {
        return planeRideId;
    }

    public void setPlaneRideId(int planeRideId) {
        this.planeRideId = planeRideId;
    }

    public int getFlightSegmentId() {
        return flightSegmentId;
    }

    public void setFlightSegmentId(int flightSegmentId) {
        this.flightSegmentId = flightSegmentId;
    }
}
