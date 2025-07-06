package com.example.odyssea.dtos.draft;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class OptionList {
    @Schema(example = "[1, 2]")
    private List<Integer> options;

    public OptionList() {
    }

    public OptionList(List<Integer> options) {
        this.options = options;
    }

    public List<Integer> getOptions() {
        return options;
    }

    public void setOptions(List<Integer> options) {
        this.options = options;
    }
}
