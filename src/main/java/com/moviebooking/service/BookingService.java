package com.moviebooking.service;

import com.moviebooking.dto.booking.SeatAvailabilityResponse;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Booking;
import com.moviebooking.model.Showtime;
import com.moviebooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowtimeService showtimeService;

    public SeatAvailabilityResponse getSeatAvailability(String showtimeId) {
        Showtime showtime = showtimeService.getShowtimeById(showtimeId);

        // Get all bookings for this showtime that are confirmed or pending payment
        List<Booking.BookingStatus> occupiedStatuses = Arrays.asList(
                Booking.BookingStatus.CONFIRMED,
                Booking.BookingStatus.PENDING_PAYMENT);

        List<Booking> bookings = bookingRepository.findByShowtimeIdAndStatusIn(showtimeId, occupiedStatuses);

        // Collect all booked seat numbers
        List<String> bookedSeats = bookings.stream()
                .flatMap(booking -> booking.getBookedSeatNumbers().stream())
                .distinct()
                .collect(Collectors.toList());

        // Generate all possible seat numbers (A1-A10, B1-B10, etc. for 100 seats)
        List<String> allSeats = generateSeatNumbers(showtime.getTotalSeats());

        // Calculate available seats
        List<String> availableSeats = allSeats.stream()
                .filter(seat -> !bookedSeats.contains(seat))
                .collect(Collectors.toList());

        return new SeatAvailabilityResponse(showtimeId, showtime.getTotalSeats(), bookedSeats, availableSeats);
    }

    public Booking createBooking(String userId, String showtimeId, List<String> seatNumbers) {
        Showtime showtime = showtimeService.getShowtimeById(showtimeId);

        // Check seat availability
        SeatAvailabilityResponse availability = getSeatAvailability(showtimeId);

        // Verify all requested seats are available
        for (String seat : seatNumbers) {
            if (!availability.getAvailableSeats().contains(seat)) {
                throw new IllegalArgumentException("Seat " + seat + " is not available");
            }
        }

        // Calculate total price
        double totalPrice = seatNumbers.size() * showtime.getTicketPrice();

        // Create booking
        Booking booking = new Booking(showtimeId, userId, seatNumbers, totalPrice);
        booking.setBookingReference("BK" + System.currentTimeMillis());

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking getBookingById(String id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
    }

    public Booking updateBookingStatus(String bookingId, Booking.BookingStatus status) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void cancelBooking(String bookingId, String userId) {
        Booking booking = getBookingById(bookingId);

        // Verify the booking belongs to the user
        if (!booking.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You can only cancel your own bookings");
        }

        // Only allow cancellation if booking is pending payment
        if (booking.getStatus() != Booking.BookingStatus.PENDING_PAYMENT) {
            throw new IllegalArgumentException("Only pending bookings can be cancelled");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByShowtime(String showtimeId) {
        return bookingRepository.findByShowtimeId(showtimeId);
    }

    private List<String> generateSeatNumbers(int totalSeats) {
        List<String> seats = new ArrayList<>();
        int seatsPerRow = 10;
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);

        for (int row = 0; row < rows; row++) {
            char rowLetter = (char) ('A' + row);
            int seatsInThisRow = Math.min(seatsPerRow, totalSeats - (row * seatsPerRow));

            for (int seat = 1; seat <= seatsInThisRow; seat++) {
                seats.add(rowLetter + String.valueOf(seat));
            }
        }

        return seats;
    }

    public List<Booking> findByBookingReference(String bookingReference) {
        return bookingRepository.findByBookingReference(bookingReference);
    }
}
