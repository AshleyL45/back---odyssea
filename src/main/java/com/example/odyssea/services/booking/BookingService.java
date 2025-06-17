package com.example.odyssea.services.booking;

import com.example.odyssea.daos.booking.BookingDraftDao;
import com.example.odyssea.daos.itinerary.ItineraryDao;
import com.example.odyssea.daos.mainTables.OptionDao;
import com.example.odyssea.daos.booking.BookingDao;
import com.example.odyssea.daos.booking.BookingOptionDao;
import com.example.odyssea.daos.booking.BookingOptionDraftDao;
import com.example.odyssea.dtos.booking.BookingConfirmation;
import com.example.odyssea.entities.booking.BookingDraft;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.entities.booking.Booking;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.exceptions.BookingNotFoundException;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingDao bookingDao;
    private final BookingOptionDao bookingOptionDao;
    private final ItineraryDao itineraryDao;
    private final OptionDao optionDao;
    private final CurrentUserService currentUserService;
    private final BookingDraftDao bookingDraftDao;
    private final BookingOptionDraftDao bookingOptionDraftDao;


    public BookingService(BookingDao bookingDao, BookingOptionDao bookingOptionDao, ItineraryDao itineraryDao, OptionDao optionDao, CurrentUserService currentUserService, BookingDraftDao bookingDraftDao, BookingOptionDraftDao bookingOptionDraftDao) {
        this.bookingDao = bookingDao;
        this.bookingOptionDao = bookingOptionDao;
        this.itineraryDao = itineraryDao;
        this.optionDao = optionDao;
        this.currentUserService = currentUserService;
        this.bookingDraftDao = bookingDraftDao;
        this.bookingOptionDraftDao = bookingOptionDraftDao;
    }


    @Transactional
    public void createBooking() {
        Integer userId = getUserId();
        BookingDraft draft = bookingDraftDao.getLastDraftByUserId(userId);

        if (draft.getUserId() == null || draft.getItineraryId() == null || draft.getDepartureDate() == null) {
            throw new ValidationException("Missing required draft data");
        }

        LocalDate returnDate = draft.getDepartureDate().plusDays(12);
        BigDecimal totalPrice = calculateTotalPrice(draft);

        Booking booking = new Booking();
        booking.setUserId(draft.getUserId());
        booking.setItineraryId(draft.getItineraryId());
        booking.setDepartureDate(draft.getDepartureDate());
        booking.setReturnDate(returnDate);
        booking.setNumberOfAdults(draft.getNumberOfAdults());
        booking.setNumberOfKids(draft.getNumberOfKids());
        booking.setType(draft.getType());
        booking.setPurchaseDate(LocalDate.now());
        booking.setTotalPrice(totalPrice);

        bookingDao.save(booking);

        List<Integer> optionIds = bookingOptionDraftDao.getOptionsByDraftId(draft.getDraftId())
                .stream().map(Option::getId).toList();

        if(!optionIds.isEmpty()){
            bookingOptionDraftDao.saveOptions(draft.getDraftId(), optionIds);
        }
    }



    public List<BookingConfirmation> getAllUserBookings() {
        StopWatch watch = new StopWatch();
        watch.start("Getting all user bookings");

        Integer userId = getUserId();

        List<BookingConfirmation> bookings =  getBookingsAndMap(bookingDao.findAllUserBookings(userId));

        watch.stop();
        System.out.println(watch.prettyPrint());
        return  bookings;
    }


    public BookingConfirmation getBookingById(int bookingId) {
        Integer userId = getUserId();
        Booking booking = bookingDao.findById(userId, bookingId);

        if (booking.getUserId() != userId) {
            throw new BookingNotFoundException("Booking does not belong to the current user.");
        }

        return createBookingConfirmation(booking);
    }


    private Integer getUserId() {
        return currentUserService.getCurrentUserId();
    }

    private List<BookingConfirmation> getBookingsAndMap(List<Booking> bookings) {
        return bookings.stream()
                .map(this::createBookingConfirmation)
                .collect(Collectors.toList());
    }

    private BookingConfirmation createBookingConfirmation(Booking booking) {
        Itinerary itinerary = itineraryDao.findById(booking.getItineraryId());
        List<Option> options = bookingOptionDao.getBookingOptions(booking.getId());
        return BookingConfirmation.fromEntities(booking, itinerary, options);
    }


    private BigDecimal calculateTotalPrice(BookingDraft draft) {
        BigDecimal itineraryPrice = itineraryDao.findById(draft.getItineraryId()).getPrice();
        BigDecimal optionPrice = bookingOptionDraftDao.getOptionsByDraftId(draft.getDraftId()).stream()
                .map(option -> optionDao.findById(option.getId()).getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return itineraryPrice
                .multiply(BigDecimal.valueOf(draft.getNumberOfAdults()))
                .add(itineraryPrice.multiply(BigDecimal.valueOf(draft.getNumberOfKids())))
                .add(optionPrice);
    }


}
