package com.moviebooking.dto.booking;

import java.util.List;

public class SeatAvailabilityResponse {

    private String showtimeId;
    private int totalSeats;
    private List<String> bookedSeats;
    private List<String> availableSeats;

    // Constructors
    public SeatAvailabilityResponse() {}

    public SeatAvailabilityResponse(String showtimeId, int totalSeats, List<String> bookedSeats, List<String> availableSeats) {
        this.showtimeId = showtimeId;
        this.totalSeats = totalSeats;
        this.bookedSeats = bookedSeats;
        this.availableSeats = availableSeats;
    }

    // Getters and Setters
    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public List<String> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(List<String> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public List<String> getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(List<String> availableSeats) {
        this.availableSeats = availableSeats;
    }
}
