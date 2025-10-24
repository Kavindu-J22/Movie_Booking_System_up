package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.booking.BookingRequest;
import com.moviebooking.dto.booking.SeatAvailabilityResponse;
import com.moviebooking.model.Booking;
import com.moviebooking.model.User;
import com.moviebooking.service.BookingService;
import com.moviebooking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/seats/{showtimeId}")
    public ResponseEntity<ApiResponse<SeatAvailabilityResponse>> getSeatAvailability(@PathVariable String showtimeId) {
        SeatAvailabilityResponse availability = bookingService.getSeatAvailability(showtimeId);
        return ResponseEntity.ok(ApiResponse.success("Seat availability retrieved successfully", availability));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Booking>> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Booking booking = bookingService.createBooking(user.getId(), request.getShowtimeId(), request.getSeatNumbers());
        return ResponseEntity.ok(ApiResponse.success("Booking created successfully", booking));
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Booking>>> getUserBookings(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        List<Booking> bookings = bookingService.getUserBookings(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Booking>> getBookingById(@PathVariable String id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(ApiResponse.success("Booking retrieved successfully", booking));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> cancelBooking(
            @PathVariable String id,
            Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        bookingService.cancelBooking(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully"));
    }
}

@RestController
@RequestMapping("/admin/bookings")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Booking>>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
    }

    @GetMapping("/showtime/{showtimeId}")
    public ResponseEntity<ApiResponse<List<Booking>>> getBookingsByShowtime(@PathVariable String showtimeId) {
        List<Booking> bookings = bookingService.getBookingsByShowtime(showtimeId);
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Booking>> updateBookingStatus(
            @PathVariable String id,
            @RequestParam Booking.BookingStatus status) {
        Booking booking = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Booking status updated successfully", booking));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Booking>>> searchBookings(@RequestParam String bookingReference) {
        List<Booking> bookings = bookingService.findByBookingReference(bookingReference);
        return ResponseEntity.ok(ApiResponse.success("Bookings found", bookings));
    }
}
