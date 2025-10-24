package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.ShowtimeWithMovieInfo;
import com.moviebooking.model.Movie;
import com.moviebooking.model.Showtime;
import com.moviebooking.service.MovieService;
import com.moviebooking.service.ShowtimeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ShowtimeService showtimeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Movie>>> getAllMovies() {
        try {
            List<Movie> movies = movieService.getAllActiveMovies();
            return ResponseEntity.ok(ApiResponse.success("Movies retrieved successfully", movies));
        } catch (Exception e) {
            // Return empty list if database is not available
            return ResponseEntity
                    .ok(ApiResponse.success("Movies retrieved successfully (database unavailable)", new ArrayList<>()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Movie>> getMovieById(@PathVariable String id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(ApiResponse.success("Movie retrieved successfully", movie));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Movie>>> searchMovies(@RequestParam String title) {
        List<Movie> movies = movieService.searchMovies(title);
        return ResponseEntity.ok(ApiResponse.success("Movies found", movies));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<ApiResponse<List<Movie>>> getMoviesByGenre(@PathVariable String genre) {
        List<Movie> movies = movieService.getMoviesByGenre(genre);
        return ResponseEntity.ok(ApiResponse.success("Movies retrieved successfully", movies));
    }

    @GetMapping("/{id}/showtimes")
    public ResponseEntity<ApiResponse<List<Showtime>>> getMovieShowtimes(@PathVariable String id) {
        // For now, show all active showtimes (including past ones for testing)
        // In production, you might want to use getUpcomingShowtimesByMovieId(id)
        List<Showtime> showtimes = showtimeService.getShowtimesByMovieId(id);
        return ResponseEntity.ok(ApiResponse.success("Showtimes retrieved successfully", showtimes));
    }
}

@RestController
@RequestMapping("/showtimes")
@CrossOrigin(origins = "*", maxAge = 3600)
class PublicShowtimeController {

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShowtimeWithMovieInfo>> getShowtimeById(@PathVariable String id) {
        Showtime showtime = showtimeService.getShowtimeById(id);

        // Get movie details to include movie title
        String movieTitle = "Unknown Movie";
        try {
            Movie movie = movieService.getMovieById(showtime.getMovieId());
            movieTitle = movie.getTitle();
        } catch (Exception e) {
            // If movie not found, keep default title
        }

        ShowtimeWithMovieInfo showtimeWithMovieInfo = new ShowtimeWithMovieInfo(
                showtime.getId(),
                showtime.getMovieId(),
                movieTitle,
                showtime.getStartTime(),
                showtime.getEndTime(),
                showtime.getScreenNumber(),
                showtime.getTotalSeats(),
                showtime.getTicketPrice(),
                showtime.isActive(),
                showtime.getCreatedAt(),
                showtime.getUpdatedAt());

        return ResponseEntity.ok(ApiResponse.success("Showtime retrieved successfully", showtimeWithMovieInfo));
    }
}

@RestController
@RequestMapping("/admin/movies")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
class AdminMovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Movie>>> getAllMoviesAdmin() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(ApiResponse.success("Movies retrieved successfully", movies));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Movie>> createMovie(
            @Valid @RequestPart("movie") Movie movie,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        Movie createdMovie = movieService.createMovie(movie, imageFile);
        return ResponseEntity.ok(ApiResponse.success("Movie created successfully", createdMovie));
    }

    @PostMapping("/json")
    public ResponseEntity<ApiResponse<Movie>> createMovieJson(@Valid @RequestBody Movie movie) throws IOException {
        Movie createdMovie = movieService.createMovie(movie, null);
        return ResponseEntity.ok(ApiResponse.success("Movie created successfully", createdMovie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Movie>> updateMovie(
            @PathVariable String id,
            @Valid @RequestPart("movie") Movie movie,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        Movie updatedMovie = movieService.updateMovie(id, movie, imageFile);
        return ResponseEntity.ok(ApiResponse.success("Movie updated successfully", updatedMovie));
    }

    @PutMapping("/{id}/json")
    public ResponseEntity<ApiResponse<Movie>> updateMovieJson(
            @PathVariable String id,
            @Valid @RequestBody Movie movie) throws IOException {
        Movie updatedMovie = movieService.updateMovie(id, movie, null);
        return ResponseEntity.ok(ApiResponse.success("Movie updated successfully", updatedMovie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMovie(@PathVariable String id) throws IOException {
        movieService.deleteMovie(id);
        return ResponseEntity.ok(ApiResponse.success("Movie deleted successfully"));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Movie>> deactivateMovie(@PathVariable String id) {
        Movie movie = movieService.deactivateMovie(id);
        return ResponseEntity.ok(ApiResponse.success("Movie deactivated successfully", movie));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Movie>> activateMovie(@PathVariable String id) {
        Movie movie = movieService.activateMovie(id);
        return ResponseEntity.ok(ApiResponse.success("Movie activated successfully", movie));
    }
}
