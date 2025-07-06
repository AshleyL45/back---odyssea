package com.example.odyssea.dtos.draft;

import io.swagger.v3.oas.annotations.media.Schema;

public class HotelStanding {
    @Schema(example = "4")
    private int hotelStanding;

    public HotelStanding() {
    }

    public HotelStanding(int hotelStanding) {
        this.hotelStanding = hotelStanding;
    }

    public int getHotelStanding() {
        return hotelStanding;
    }

    public void setHotelStanding(int hotelStanding) {
        this.hotelStanding = hotelStanding;
    }
}
