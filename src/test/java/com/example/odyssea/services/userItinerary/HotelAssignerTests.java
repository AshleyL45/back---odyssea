package com.example.odyssea.services.userItinerary;

import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.services.mainTables.HotelService;
import com.example.odyssea.services.userItinerary.helpers.HotelAssigner;
import com.example.odyssea.services.userItinerary.helpers.LocationAssigner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class HotelAssignerTests {
    private HotelService hotelServiceMock;
    private HotelAssigner hotelAssigner;

    @BeforeEach
    void setUp() {
        hotelServiceMock = mock(HotelService.class);
        hotelAssigner = new HotelAssigner(hotelServiceMock);
    }

    @Test
    void testAssignHotelForMiddleDay(){
        Hotel hotel1 = new Hotel(1, 2, "Hotel 1", 5, "A description 1", 150.00);
        Hotel hotel2 = new Hotel(25, 8, "Hotel 2", 5, "A description 2", 180.00);

        List<Hotel> hotels = List.of(hotel1, hotel2);

        UserItineraryDayDTO day = new UserItineraryDayDTO();

        day.setDayNumber(5);

        Hotel assignedHotel = hotelAssigner.assignHotel(day, hotels);
        assertEquals("Hotel 2", assignedHotel.getName());
    }
}
