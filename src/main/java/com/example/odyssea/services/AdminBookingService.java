package com.example.odyssea.services;

import com.example.odyssea.daos.itinerary.ItineraryDao;
import com.example.odyssea.daos.booking.BookingDao;
import com.example.odyssea.daos.booking.BookingOptionDao;
import com.example.odyssea.daos.userAuth.UserDao;
import com.example.odyssea.daos.userItinerary.UserItineraryDao;
import com.example.odyssea.dtos.booking.AdminBookingConfirmation;
import com.example.odyssea.dtos.booking.AdminBookingConfirmationDetails;
import com.example.odyssea.dtos.booking.AdminUserItineraryDetails;
import com.example.odyssea.dtos.userItinerary.UserItineraryDTO;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.entities.booking.Booking;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.enums.BookingStatus;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.userItinerary.UserItineraryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class AdminBookingService {
    private final UserDao userDao;
    private final BookingDao bookingDao;
    private final ItineraryDao itineraryDao;
    private final BookingOptionDao bookingOptionDao;
    private final UserItineraryDao userItineraryDao;
    private final UserItineraryService userItineraryService;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("startDate", "booking_date");
    private static final Set<String> ALLOWED_SORT_BOOKING_FIELDS = Set.of("departureDate", "purchaseDate");
    private static final Set<String> ALLOWED_SORT_DIRECTIONS = Set.of("asc", "desc");

    public AdminBookingService(UserDao userDao, BookingDao bookingDao, ItineraryDao itineraryDao, BookingOptionDao bookingOptionDao, UserItineraryDao userItineraryDao, UserItineraryService userItineraryService) {
        this.userDao = userDao;
        this.bookingDao = bookingDao;
        this.itineraryDao = itineraryDao;
        this.bookingOptionDao = bookingOptionDao;
        this.userItineraryDao = userItineraryDao;
        this.userItineraryService = userItineraryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminBookingConfirmation> getAllUserItinerariesAndFilter(String status, String search, String sortField, String sortDirection){
        if(status != null){
            boolean isValid = BookingStatus.contains(status);
            if(!isValid){
                throw new ValidationException("Booking status is invalid.");
            }
        }

        validatePersonalizedSorting(sortField, sortDirection);

        List<UserItinerary> bookings = userItineraryDao.getAllUserItinerariesAndFilter(status, search, sortField, sortDirection);
        List <AdminBookingConfirmation> adminBookingConfirmations = bookings.stream()
                .map(this::fromUserItinerary)
                .toList();
        return adminBookingConfirmations;
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminBookingConfirmation> getAllBookingsAndFilter(String status, String search, String sortField, String sortDirection){
        if(status != null){
            boolean isValid = BookingStatus.contains(status);
            if(!isValid){
                throw new ValidationException("Booking status is invalid.");
            }
        }

        validateStandardSorting(sortField, sortDirection);

        List<Booking> bookings = bookingDao.getAllBookingsAndFilter(status, search, sortField, sortDirection);
        List <AdminBookingConfirmation> adminBookingConfirmationDetails = bookings.stream()
                .map(this::fromBooking)
                .toList();
        return adminBookingConfirmationDetails;
    }


    @PreAuthorize("hasRole('ADMIN')")
    public AdminBookingConfirmationDetails getBookingByIdForAdmin(int bookingId) {
        Booking booking = bookingDao.findByBookingId(bookingId);
        return convertToAdminBookingConfirmationDetails(booking);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AdminUserItineraryDetails getByUserItineraryId(int bookingId) {
        UserItineraryDTO userItineraryDTO = userItineraryService.getAUserItineraryById(bookingId);
        User user = userDao.findById(userItineraryDTO.getUserId());
        return convertToAdminUserItineraryDetails(user, userItineraryDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateBooking(int bookingId, BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Total price must be a positive number.");
        }

        bookingDao.updateBooking(bookingId, newPrice);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public void updateBooking(int bookingId, BookingStatus status) {
        bookingDao.updateBooking(bookingId, status);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateUserItineraryStatus(int bookingId, BookingStatus status) {
        userItineraryDao.updateStatus(bookingId, status);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updatePrice(int itineraryId, BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Price must be a positive number.");
        }
        userItineraryDao.updatePrice(itineraryId, newPrice);
    }




    private AdminUserItineraryDetails convertToAdminUserItineraryDetails(User user, UserItineraryDTO userItineraryDTO){
        return AdminUserItineraryDetails.fromEntities(user, userItineraryDTO);
    }

    private AdminBookingConfirmationDetails convertToAdminBookingConfirmationDetails(Booking booking){
        Booking bookingFound = bookingDao.findByBookingId(booking.getBooking());
        User user = userDao.findById(booking.getUserId());
        Itinerary itinerary = itineraryDao.findById(bookingFound.getItineraryId());
        List<Option> options = bookingOptionDao.getBookingOptions(bookingFound.getBooking());

        return AdminBookingConfirmationDetails.fromEntities(user, itinerary, bookingFound, options);

    }

    private AdminBookingConfirmation fromBooking(Booking booking){
        Integer id = booking.getBooking();
        BigDecimal price = booking.getTotalPrice();
        LocalDate purchaseDate = booking.getPurchaseDate();
        BookingStatus status = BookingStatus.valueOf(booking.getStatus());
        User user = userDao.findById(booking.getUserId());

        return AdminBookingConfirmation.fromEntities(id, user,price, purchaseDate, status);

    }

    private AdminBookingConfirmation fromUserItinerary(UserItinerary itinerary){
        Integer id = itinerary.getId();
        LocalDate purchaseDate = itinerary.getBookingDate();
        User user = userDao.findById(itinerary.getUserId());
        BigDecimal price = itinerary.getStartingPrice();
        BookingStatus status = itinerary.getStatus();

        return AdminBookingConfirmation.fromEntities(id, user, price, purchaseDate, status);
    }

    private void validateStandardSorting(String sortField, String sortDirection) {
        validateSorting(sortField, sortDirection, ALLOWED_SORT_BOOKING_FIELDS);
    }

    private void validatePersonalizedSorting(String sortField, String sortDirection) {
        validateSorting(sortField, sortDirection, ALLOWED_SORT_FIELDS);
    }

    private void validateSorting(String sortField, String sortDirection, Set<String> allowedFields) {
        if (sortField != null || sortDirection != null) {
            if (sortField == null || sortDirection == null) {
                throw new ValidationException("Both sort field and sort direction must be provided together.");
            }
            if (!allowedFields.contains(sortField)) {
                throw new ValidationException("Sort field is invalid.");
            }
            if (!ALLOWED_SORT_DIRECTIONS.contains(sortDirection)) {
                throw new ValidationException("Sort direction is invalid.");
            }
        }
    }

}
