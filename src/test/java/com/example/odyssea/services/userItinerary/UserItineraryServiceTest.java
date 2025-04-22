package com.example.odyssea.services.userItinerary;

import com.example.odyssea.services.CurrentUserService;
import com.example.odyssea.services.mainTables.HotelService;
import com.example.odyssea.services.userItinerary.helpers.HotelAssigner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UserItineraryServiceTest {
    private UserItineraryService userItineraryService;

   /* @BeforeEach
    void setUp() {
        UserItineraryDraftService userItineraryDraftServiceMock = mock(UserItineraryDraftService.class);
        userItineraryService = new UserItineraryService(userItineraryDraftServiceMock);
    }*/

    @InjectMocks
    CurrentUserService currentUserService = new CurrentUserService();


}
