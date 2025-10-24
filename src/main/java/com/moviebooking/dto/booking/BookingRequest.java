package com.moviebooking.dto.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class BookingRequest {

    @NotBlank(message = "Showtime ID is required")
    private String showtimeId;

    @NotEmpty(message = "At least one seat must be selected")
    private List<String> seatNumbers;

    // Constructors
    public BookingRequest() {}

    public BookingRequest(String showtimeId, List<String> seatNumbers) {
        this.showtimeId = showtimeId;
        this.seatNumbers = seatNumbers;
    }

    // Getters and Setters
    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(List<String> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }
}
