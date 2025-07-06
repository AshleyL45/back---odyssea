package com.example.odyssea.services.booking.draft;

import com.example.odyssea.daos.booking.BookingDraftDao;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;

@Service
public class BookingDraftStep2Service {
    private final BookingDraftDao bookingDraftDao;
    private final CurrentUserService currentUserService;

    public BookingDraftStep2Service(BookingDraftDao bookingDraftDao,
                                    CurrentUserService currentUserService) {
        this.bookingDraftDao = bookingDraftDao;
        this.currentUserService = currentUserService;
    }

    private Integer getUserId() {
        return currentUserService.getCurrentUserId();
    }

    /**
     * Étape 2 : validation nombre d’adultes et d’enfants
     */
    public void execute(Integer numberAdults, Integer numberKids) {
        if (numberAdults == null || numberAdults <= 0) {
            throw new ValidationException("There must be at least one adult");
        }
        if (numberKids == null || numberKids < 0) {
            throw new ValidationException("Number of kids cannot be negative");
        }

        Integer draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
        bookingDraftDao.updateNumberOfAdults(draftId, numberAdults);
        bookingDraftDao.updateNumberOfKids(draftId, numberKids);
    }
}
