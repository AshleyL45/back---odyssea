package com.example.odyssea.entities.mainTables;

public class FlightSegmentRide {
    private int planeRideId;
    private int flightSegmentId;

    public FlightSegmentRide() {}

    public FlightSegmentRide(int planeRideId, int flightSegmentId) {
        this.planeRideId = planeRideId;
        this.flightSegmentId = flightSegmentId;
    }

    // Getters et setters

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
