package com.example.odyssea.dtos.Flight;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AircraftDTO {

    @JsonProperty("code")
    private String code;

    public AircraftDTO(String code) {
        this.code = code;
    }

    public AircraftDTO() {
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
