package com.moviebooking.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Document(collection = "showtimes")
public class Showtime extends BaseEntity {

    @NotBlank(message = "Movie ID is required")
    private String movieId;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @NotNull(message = "Screen number is required")
    @Positive(message = "Screen number must be positive")
    private Integer screenNumber;

    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    private Integer totalSeats = 100; // Default 100 seats

    @NotNull(message = "Ticket price is required")
    @Positive(message = "Ticket price must be positive")
    private Double ticketPrice;

    private boolean active = true;

    // Constructors
    public Showtime() {}

    public Showtime(String movieId, LocalDateTime startTime, LocalDateTime endTime, 
                   Integer screenNumber, Double ticketPrice) {
        this.movieId = movieId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.screenNumber = screenNumber;
        this.ticketPrice = ticketPrice;
    }

    // Getters and Setters
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getScreenNumber() {
        return screenNumber;
    }

    public void setScreenNumber(Integer screenNumber) {
        this.screenNumber = screenNumber;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
