package com.moviebooking.dto.report;

import java.time.LocalDate;
import java.util.List;

public class SalesReportResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalRevenue;
    private Integer totalBookings;
    private Integer totalTicketsSold;
    private List<DailySales> dailySales;

    // Constructors
    public SalesReportResponse() {}

    public SalesReportResponse(LocalDate startDate, LocalDate endDate, Double totalRevenue, 
                              Integer totalBookings, Integer totalTicketsSold, List<DailySales> dailySales) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalRevenue = totalRevenue;
        this.totalBookings = totalBookings;
        this.totalTicketsSold = totalTicketsSold;
        this.dailySales = dailySales;
    }

    // Getters and Setters
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(Integer totalBookings) {
        this.totalBookings = totalBookings;
    }

    public Integer getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public void setTotalTicketsSold(Integer totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
    }

    public List<DailySales> getDailySales() {
        return dailySales;
    }

    public void setDailySales(List<DailySales> dailySales) {
        this.dailySales = dailySales;
    }

    public static class DailySales {
        private LocalDate date;
        private Double revenue;
        private Integer bookings;
        private Integer ticketsSold;

        public DailySales() {}

        public DailySales(LocalDate date, Double revenue, Integer bookings, Integer ticketsSold) {
            this.date = date;
            this.revenue = revenue;
            this.bookings = bookings;
            this.ticketsSold = ticketsSold;
        }

        // Getters and Setters
        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public Double getRevenue() {
            return revenue;
        }

        public void setRevenue(Double revenue) {
            this.revenue = revenue;
        }

        public Integer getBookings() {
            return bookings;
        }

        public void setBookings(Integer bookings) {
            this.bookings = bookings;
        }

        public Integer getTicketsSold() {
            return ticketsSold;
        }

        public void setTicketsSold(Integer ticketsSold) {
            this.ticketsSold = ticketsSold;
        }
    }
}
