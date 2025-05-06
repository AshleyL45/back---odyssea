package com.example.odyssea.exceptions;

import com.example.odyssea.exceptions.JwtToken.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Cette donnée n'est pas valide",
                exception.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFoundError(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error 404: The requested URL was not found on this server.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<String> handleReservationNotFound(ReservationNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDuration(ValidationException e){
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Provided data is not valid",
                e.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid JSON", "Malformed or invalid JSON body"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseError(DatabaseException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,"Internal error", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CityNotFound.class)
    public ResponseEntity<ErrorResponse> handleCityNotFound(CityNotFound ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid city", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CountryNotFound.class)
    public ResponseEntity<ErrorResponse> handleCountryNotFound(CountryNotFound ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid country", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ActivityNotFound.class)
    public ResponseEntity<ErrorResponse> handleActivityNotFound(ActivityNotFound ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid activity or activities", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OptionNotFound.class)
    public ResponseEntity<ErrorResponse> handleOptionNotFound(OptionNotFound ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid option", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HotelNotFound.class)
    public ResponseEntity<ErrorResponse> handleHotelNotFound(HotelNotFound ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                "Hotel not found",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserItineraryDatabaseException.class)
    public ResponseEntity<ErrorResponse> handleUserItineraryDatabaseException(UserItineraryDatabaseException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred while saving the personalized itinerary",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FlightSegmentsNotFound.class)
    public ResponseEntity<ErrorResponse> handleFlightSegmentNotFound(FlightSegmentsNotFound ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An internal error occurred. Flight segment not found.",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtTokenMalformedException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwt(JwtTokenMalformedException ex) {
        ErrorResponse err = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid token",
                ex.getMessage()
        );
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(JwtTokenExpiredException ex) {
        ErrorResponse err = new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Expired token",
                ex.getMessage()
        );
        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({JwtTokenUnsupportedException.class, JwtTokenSignatureException.class, JwtTokenMissingException.class})
    public ResponseEntity<ErrorResponse> handleOtherJwtErrors(RuntimeException ex) {
        ErrorResponse err = new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "JWT authentication error",
                ex.getMessage()
        );
        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SelectionAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleSelectionAlreadyExist(SelectionAlreadyExistException ex) {
        ErrorResponse err = new ErrorResponse(
                HttpStatus.CONFLICT,
                "This itinerary is already in your selection.",
                ex.getMessage()
        );
        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SelectionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSelectionNotFound(SelectionNotFoundException ex) {
        ErrorResponse err = new ErrorResponse(
                HttpStatus.CONFLICT,
                "This itinerary is not in your selection.",
                ex.getMessage()
        );
        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidBookingStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBookingStatus(InvalidBookingStatusException ex) {
        ErrorResponse err = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Booking status must be PENDING, CONFIRMED or CANCELLED",
                ex.getMessage()
        );
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
}
