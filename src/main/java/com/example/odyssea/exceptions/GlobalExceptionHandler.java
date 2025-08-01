package com.example.odyssea.exceptions;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.exceptions.JwtToken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidDataException(ValidationException e) {
        logger.error("Validation exception : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST)
        );
    }

    @ExceptionHandler({
            ResourceNotFoundException.class,
            BookingNotFoundException.class,
            CountryNotFound.class,
            OptionNotFound.class,
            SelectionNotFoundException.class,
            FlightNotFoundException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleNotFound(RuntimeException ex) {
        logger.error("Not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error("Resource not found.", HttpStatus.NOT_FOUND)
        );
    }

    @ExceptionHandler(SelectionAlreadyExistException.class)
    public ResponseEntity<ApiResponse<Void>> handleSelectionAlreadyExists(SelectionAlreadyExistException ex) {
        logger.error("Selection already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponse.error("Selection already exists.", HttpStatus.CONFLICT)
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        logger.error("User already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error("User already exists.", HttpStatus.BAD_REQUEST)
        );
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidPassword(InvalidPasswordException ex) {
        logger.error("Invalid password: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error("Invalid password.", HttpStatus.BAD_REQUEST)
        );
    }

    @ExceptionHandler(InvalidBookingStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidBookingStatus(InvalidBookingStatusException ex) {
        logger.error("Invalid booking status: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error("Booking status must be PENDING, CONFIRMED or CANCELLED.", HttpStatus.BAD_REQUEST)
        );
    }

    @ExceptionHandler(UserItineraryDatabaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleItinerarySaveError(UserItineraryDatabaseException ex) {
        logger.error("Database error saving itinerary: {}", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("An error occurred while saving the itinerary.", HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    @ExceptionHandler(FlightSegmentsNotFound.class)
    public ResponseEntity<ApiResponse<Void>> handleFlightSegmentsNotFound(FlightSegmentsNotFound ex) {
        logger.error("Flight segment not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("Flight segment not found.", HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    @ExceptionHandler(CityNotFound.class)
    public ResponseEntity<ApiResponse<Void>> handleCityNotFound(CityNotFound ex) {
        logger.error("City not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ApiResponse<Void>> handleExternalService(ExternalServiceException ex) {
        logger.error("External service error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error(
                        "Service temporairement indisponible, réessayez plus tard.",
                        HttpStatus.SERVICE_UNAVAILABLE));
    }

    @ExceptionHandler(ActivityNotFound.class)
    public ResponseEntity<ApiResponse<Void>> handleActivityNotFound(ActivityNotFound ex) {
        logger.error("Activity not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(HotelNotFound.class)
    public ResponseEntity<ApiResponse<Void>> handleHotelNotFound(HotelNotFound ex) {
        logger.error("Hotel not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND));
    }


    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleImageNotFound(ImageNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<ApiResponse<Void>> handleProcessingError(ImageProcessingException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.error("Error processing image: " + ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR));
    }


    @ExceptionHandler(JwtTokenMalformedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMalformedJwt(JwtTokenMalformedException ex) {
        logger.error("Invalid JWT: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error("Invalid token.", HttpStatus.BAD_REQUEST)
        );
    }

    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleExpiredJwt(JwtTokenExpiredException ex) {
        logger.error("Expired JWT: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.error("Expired token.", HttpStatus.UNAUTHORIZED)
        );
    }

    @ExceptionHandler({JwtTokenUnsupportedException.class, JwtTokenSignatureException.class, JwtTokenMissingException.class, InsufficientAuthenticationException.class})
    public ResponseEntity<ApiResponse<Void>> handleOtherJwtErrors(RuntimeException ex) {
        logger.error("JWT authentication error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.error("JWT authentication error.", HttpStatus.UNAUTHORIZED)
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidJson(HttpMessageNotReadableException ex) {
        logger.error("Invalid JSON input: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error("Invalid or malformed JSON.", HttpStatus.BAD_REQUEST)
        );
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleDatabaseError(DatabaseException ex) {
        logger.error("Database error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandler(NoHandlerFoundException ex) {
        logger.error("404 not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error("The requested URL was not found.", HttpStatus.NOT_FOUND)
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(errorMessage, HttpStatus.BAD_REQUEST));
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoEndpointFound(NoResourceFoundException ex) {
        logger.error("Endpoint not found: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error("Invalid endpoint.", HttpStatus.BAD_REQUEST)
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        logger.error("Authentication error : {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.error("Invalid credentials.", HttpStatus.UNAUTHORIZED)
        );
    }


    @ExceptionHandler({UsernameNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(Exception ex) {
        logger.error("User not found : {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.error("User not found.", HttpStatus.UNAUTHORIZED)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobal(Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

}
