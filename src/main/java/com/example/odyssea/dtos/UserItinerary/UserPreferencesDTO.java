package com.example.odyssea.dtos.UserItinerary;

import com.example.odyssea.entities.mainTables.Option;

import java.time.LocalDate;
import java.util.List;

public class UserPreferencesDTO {
    private LocalDate startDate;
    private String departureCity;
    private List<CountrySelectionDTO>  countrySelection;
    private int numberOfAdults;
    private int numberOfKids;
    private int hotelStanding;
    private List<Option> options;
    private String itineraryName;
    // Activities per city
}
