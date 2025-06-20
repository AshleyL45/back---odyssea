package com.example.odyssea.services.booking.draft;

import com.example.odyssea.daos.booking.BookingDraftDao;
import com.example.odyssea.daos.booking.BookingOptionDraftDao;
import com.example.odyssea.daos.mainTables.OptionDao;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingDraftStep3Service {
    private final BookingDraftDao bookingDraftDao;
    private final BookingOptionDraftDao bookingOptionDraftDao;
    private final OptionDao optionDao;
    private final CurrentUserService currentUserService;

    public BookingDraftStep3Service(BookingDraftDao bookingDraftDao,
                                    BookingOptionDraftDao bookingOptionDraftDao,
                                    OptionDao optionDao,
                                    CurrentUserService currentUserService) {
        this.bookingDraftDao = bookingDraftDao;
        this.bookingOptionDraftDao = bookingOptionDraftDao;
        this.optionDao = optionDao;
        this.currentUserService = currentUserService;
    }

    private Integer getUserId() {
        return currentUserService.getCurrentUserId();
    }

    /**
     * Étape 3 : enregistre la liste d’options choisies
     */
    @Transactional
    public void execute(List<Integer> optionIds) {
        Integer draftId = bookingDraftDao.getLastDraftIdByUser(getUserId());
        // purge des anciennes options
        bookingOptionDraftDao.deleteOptionsByDraftId(draftId);

        if (optionIds == null || optionIds.isEmpty()) {
            return;
        }

        // validation existence de chaque option
        for (Integer optId : optionIds) {
            if (optionDao.findById(optId) == null) {
                throw new ValidationException("Option not found: " + optId);
            }
        }

        bookingOptionDraftDao.saveOptions(draftId, optionIds);
    }
}
