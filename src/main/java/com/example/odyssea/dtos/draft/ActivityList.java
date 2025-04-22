package com.example.odyssea.dtos.draft;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ActivityList {
    @Schema(example = "[25, 29]")
    private List<Integer> activities;

    public ActivityList() {
    }

    public ActivityList(List<Integer> activities) {
        this.activities = activities;
    }

    public List<Integer> getActivities() {
        return activities;
    }

    public void setActivities(List<Integer> activities) {
        this.activities = activities;
    }
}
