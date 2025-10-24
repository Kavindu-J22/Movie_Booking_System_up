package com.moviebooking.model;

import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Document(collection = "bookings")
public class Booking extends BaseEntity {

    @NotBlank(message = "Showtime ID is required")
    private String showtimeId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotEmpty(message = "At least one seat must be selected")
    private List<String> bookedSeatNumbers;

    @NotNull(message = "Total price is required")
    @Positive(message = "Total price must be positive")
    private Double totalPrice;

    private BookingStatus status = BookingStatus.PENDING_PAYMENT;

    private String bookingReference;

    // Constructors
    public Booking() {}

    public Booking(String showtimeId, String userId, List<String> bookedSeatNumbers, Double totalPrice) {
        this.showtimeId = showtimeId;
        this.userId = userId;
        this.bookedSeatNumbers = bookedSeatNumbers;
        this.totalPrice = totalPrice;
        this.bookingReference = generateBookingReference();
    }

    // Getters and Setters
    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getBookedSeatNumbers() {
        return bookedSeatNumbers;
    }

    public void setBookedSeatNumbers(List<String> bookedSeatNumbers) {
        this.bookedSeatNumbers = bookedSeatNumbers;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    private String generateBookingReference() {
        return "BK" + System.currentTimeMillis();
    }

    public enum BookingStatus {
        PENDING_PAYMENT,
        CONFIRMED,
        CANCELLED,
        EXPIRED
    }
}
