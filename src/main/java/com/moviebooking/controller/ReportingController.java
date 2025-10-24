package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.report.OccupancyReportResponse;
import com.moviebooking.dto.report.SalesReportResponse;
import com.moviebooking.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/admin/reports")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    @GetMapping("/sales")
    public ResponseEntity<ApiResponse<SalesReportResponse>> getSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        SalesReportResponse report = reportingService.generateSalesReport(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Sales report generated successfully", report));
    }

    @GetMapping("/occupancy")
    public ResponseEntity<ApiResponse<OccupancyReportResponse>> getOccupancyReport() {
        OccupancyReportResponse report = reportingService.generateOccupancyReport();
        return ResponseEntity.ok(ApiResponse.success("Occupancy report generated successfully", report));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = reportingService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Dashboard statistics retrieved successfully", stats));
    }
}
