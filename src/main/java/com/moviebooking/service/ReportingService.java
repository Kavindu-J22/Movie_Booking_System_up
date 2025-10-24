package com.moviebooking.service;

import com.moviebooking.dto.report.OccupancyReportResponse;
import com.moviebooking.dto.report.SalesReportResponse;
import com.moviebooking.model.Booking;
import com.moviebooking.model.Movie;
import com.moviebooking.model.Payment;
import com.moviebooking.model.Showtime;
import com.moviebooking.repository.BookingRepository;
import com.moviebooking.repository.PaymentRepository;
import com.moviebooking.repository.ShowtimeRepository;
import com.moviebooking.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportingService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public SalesReportResponse generateSalesReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // Get successful payments in the date range
        List<Payment> payments = paymentRepository.findByProcessedAtBetween(startDateTime, endDateTime)
                .stream()
                .filter(payment -> payment.getStatus() == Payment.PaymentStatus.SUCCESS)
                .collect(Collectors.toList());

        // Calculate totals
        double totalRevenue = payments.stream().mapToDouble(Payment::getAmount).sum();
        int totalBookings = payments.size();

        // Get booking details to calculate total tickets sold
        List<String> bookingIds = payments.stream().map(Payment::getBookingId).collect(Collectors.toList());
        List<Booking> bookings = bookingRepository.findAllById(bookingIds);
        int totalTicketsSold = bookings.stream().mapToInt(booking -> booking.getBookedSeatNumbers().size()).sum();

        // Generate daily sales breakdown
        Map<LocalDate, List<Payment>> paymentsByDate = payments.stream()
                .collect(Collectors.groupingBy(payment -> payment.getProcessedAt().toLocalDate()));

        List<SalesReportResponse.DailySales> dailySales = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<Payment> dayPayments = paymentsByDate.getOrDefault(date, new ArrayList<>());
            double dayRevenue = dayPayments.stream().mapToDouble(Payment::getAmount).sum();
            int dayBookings = dayPayments.size();
            
            List<String> dayBookingIds = dayPayments.stream().map(Payment::getBookingId).collect(Collectors.toList());
            List<Booking> dayBookingList = bookingRepository.findAllById(dayBookingIds);
            int dayTicketsSold = dayBookingList.stream().mapToInt(booking -> booking.getBookedSeatNumbers().size()).sum();

            dailySales.add(new SalesReportResponse.DailySales(date, dayRevenue, dayBookings, dayTicketsSold));
        }

        return new SalesReportResponse(startDate, endDate, totalRevenue, totalBookings, totalTicketsSold, dailySales);
    }

    public OccupancyReportResponse generateOccupancyReport() {
        List<Showtime> showtimes = showtimeRepository.findByActiveTrue();
        List<OccupancyReportResponse.ShowtimeOccupancy> occupancies = new ArrayList<>();

        for (Showtime showtime : showtimes) {
            // Get confirmed bookings for this showtime
            List<Booking> confirmedBookings = bookingRepository.findByShowtimeIdAndStatusIn(
                    showtime.getId(), 
                    Arrays.asList(Booking.BookingStatus.CONFIRMED)
            );

            // Calculate booked seats
            int bookedSeats = confirmedBookings.stream()
                    .mapToInt(booking -> booking.getBookedSeatNumbers().size())
                    .sum();

            // Get movie title
            Movie movie = movieRepository.findById(showtime.getMovieId()).orElse(null);
            String movieTitle = movie != null ? movie.getTitle() : "Unknown Movie";

            OccupancyReportResponse.ShowtimeOccupancy occupancy = 
                    new OccupancyReportResponse.ShowtimeOccupancy(
                            showtime.getId(),
                            movieTitle,
                            showtime.getStartTime(),
                            showtime.getScreenNumber(),
                            showtime.getTotalSeats(),
                            bookedSeats
                    );

            occupancies.add(occupancy);
        }

        // Calculate average occupancy rate
        double averageOccupancyRate = occupancies.stream()
                .mapToDouble(OccupancyReportResponse.ShowtimeOccupancy::getOccupancyRate)
                .average()
                .orElse(0.0);

        return new OccupancyReportResponse(occupancies, averageOccupancyRate, showtimes.size());
    }

    public Map<String, Object> getDashboardStats() {
        // Total movies
        long totalMovies = movieRepository.count();
        long activeMovies = movieRepository.findByActiveTrue().size();

        // Total bookings
        long totalBookings = bookingRepository.count();
        long confirmedBookings = bookingRepository.findByStatus(Booking.BookingStatus.CONFIRMED).size();

        // Total revenue (successful payments)
        double totalRevenue = paymentRepository.findByStatus(Payment.PaymentStatus.SUCCESS)
                .stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        // Recent bookings (last 7 days)
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        List<Booking> recentBookings = bookingRepository.findAll()
                .stream()
                .filter(booking -> booking.getCreatedAt().isAfter(weekAgo))
                .collect(Collectors.toList());

        return Map.of(
                "totalMovies", totalMovies,
                "activeMovies", activeMovies,
                "totalBookings", totalBookings,
                "confirmedBookings", confirmedBookings,
                "totalRevenue", totalRevenue,
                "recentBookingsCount", recentBookings.size()
        );
    }
}
