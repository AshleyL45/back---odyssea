package com.example.odyssea.dtos.draft;

import io.swagger.v3.oas.annotations.media.Schema;

public class DurationDate {
    @Schema(example = "17")
    private Integer duration;
    @Schema(example = "01/06/2027")
    private String startDate;


    public DurationDate() {
    }

    public DurationDate(Integer duration, String startDate) {
        this.duration = duration;
        this.startDate = startDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
