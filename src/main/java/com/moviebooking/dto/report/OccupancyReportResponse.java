package com.moviebooking.dto.report;

import java.time.LocalDateTime;
import java.util.List;

public class OccupancyReportResponse {

    private List<ShowtimeOccupancy> showtimeOccupancies;
    private Double averageOccupancyRate;
    private Integer totalShowtimes;

    // Constructors
    public OccupancyReportResponse() {}

    public OccupancyReportResponse(List<ShowtimeOccupancy> showtimeOccupancies, 
                                  Double averageOccupancyRate, Integer totalShowtimes) {
        this.showtimeOccupancies = showtimeOccupancies;
        this.averageOccupancyRate = averageOccupancyRate;
        this.totalShowtimes = totalShowtimes;
    }

    // Getters and Setters
    public List<ShowtimeOccupancy> getShowtimeOccupancies() {
        return showtimeOccupancies;
    }

    public void setShowtimeOccupancies(List<ShowtimeOccupancy> showtimeOccupancies) {
        this.showtimeOccupancies = showtimeOccupancies;
    }

    public Double getAverageOccupancyRate() {
        return averageOccupancyRate;
    }

    public void setAverageOccupancyRate(Double averageOccupancyRate) {
        this.averageOccupancyRate = averageOccupancyRate;
    }

    public Integer getTotalShowtimes() {
        return totalShowtimes;
    }

    public void setTotalShowtimes(Integer totalShowtimes) {
        this.totalShowtimes = totalShowtimes;
    }

    public static class ShowtimeOccupancy {
        private String showtimeId;
        private String movieTitle;
        private LocalDateTime startTime;
        private Integer screenNumber;
        private Integer totalSeats;
        private Integer bookedSeats;
        private Double occupancyRate;

        public ShowtimeOccupancy() {}

        public ShowtimeOccupancy(String showtimeId, String movieTitle, LocalDateTime startTime,
                               Integer screenNumber, Integer totalSeats, Integer bookedSeats) {
            this.showtimeId = showtimeId;
            this.movieTitle = movieTitle;
            this.startTime = startTime;
            this.screenNumber = screenNumber;
            this.totalSeats = totalSeats;
            this.bookedSeats = bookedSeats;
            this.occupancyRate = totalSeats > 0 ? (double) bookedSeats / totalSeats * 100 : 0.0;
        }

        // Getters and Setters
        public String getShowtimeId() {
            return showtimeId;
        }

        public void setShowtimeId(String showtimeId) {
            this.showtimeId = showtimeId;
        }

        public String getMovieTitle() {
            return movieTitle;
        }

        public void setMovieTitle(String movieTitle) {
            this.movieTitle = movieTitle;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
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

        public Integer getBookedSeats() {
            return bookedSeats;
        }

        public void setBookedSeats(Integer bookedSeats) {
            this.bookedSeats = bookedSeats;
            // Recalculate occupancy rate
            this.occupancyRate = totalSeats > 0 ? (double) bookedSeats / totalSeats * 100 : 0.0;
        }

        public Double getOccupancyRate() {
            return occupancyRate;
        }

        public void setOccupancyRate(Double occupancyRate) {
            this.occupancyRate = occupancyRate;
        }
    }
}
