package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.ShowtimeWithMovieInfo;
import com.moviebooking.model.Showtime;
import com.moviebooking.service.ShowtimeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/showtimes")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
public class ShowtimeController {

    @Autowired
    private ShowtimeService showtimeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShowtimeWithMovieInfo>>> getAllShowtimes() {
        List<ShowtimeWithMovieInfo> showtimes = showtimeService.getAllShowtimesWithMovieInfo();
        return ResponseEntity.ok(ApiResponse.success("Showtimes retrieved successfully", showtimes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Showtime>> getShowtimeById(@PathVariable String id) {
        Showtime showtime = showtimeService.getShowtimeById(id);
        return ResponseEntity.ok(ApiResponse.success("Showtime retrieved successfully", showtime));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Showtime>> createShowtime(@Valid @RequestBody Showtime showtime) {
        Showtime createdShowtime = showtimeService.createShowtime(showtime);
        return ResponseEntity.ok(ApiResponse.success("Showtime created successfully", createdShowtime));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Showtime>> updateShowtime(
            @PathVariable String id,
            @Valid @RequestBody Showtime showtime) {
        Showtime updatedShowtime = showtimeService.updateShowtime(id, showtime);
        return ResponseEntity.ok(ApiResponse.success("Showtime updated successfully", updatedShowtime));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteShowtime(@PathVariable String id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.ok(ApiResponse.success("Showtime deleted successfully"));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Showtime>> deactivateShowtime(@PathVariable String id) {
        Showtime showtime = showtimeService.deactivateShowtime(id);
        return ResponseEntity.ok(ApiResponse.success("Showtime deactivated successfully", showtime));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Showtime>> activateShowtime(@PathVariable String id) {
        Showtime showtime = showtimeService.activateShowtime(id);
        return ResponseEntity.ok(ApiResponse.success("Showtime activated successfully", showtime));
    }
}
