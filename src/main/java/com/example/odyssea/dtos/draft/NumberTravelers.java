package com.example.odyssea.dtos.draft;

import io.swagger.v3.oas.annotations.media.Schema;

public class NumberTravelers {
    @Schema(example = "6")
    private Integer numberAdults;
    @Schema(example = "2")
    private Integer numberKids;

    public NumberTravelers() {
    }

    public NumberTravelers(Integer numberAdults, Integer numberKids) {
        this.numberAdults = numberAdults;
        this.numberKids = numberKids;
    }

    public Integer getNumberAdults() {
        return numberAdults;
    }

    public void setNumberAdults(Integer numberAdults) {
        this.numberAdults = numberAdults;
    }

    public Integer getNumberKids() {
        return numberKids;
    }

    public void setNumberKids(Integer numberKids) {
        this.numberKids = numberKids;
    }
}
