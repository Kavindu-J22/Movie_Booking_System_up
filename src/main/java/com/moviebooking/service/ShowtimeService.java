package com.moviebooking.service;

import com.moviebooking.dto.ShowtimeWithMovieInfo;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Movie;
import com.moviebooking.model.Showtime;
import com.moviebooking.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieService movieService;

    public List<Showtime> getAllActiveShowtimes() {
        return showtimeRepository.findByActiveTrue();
    }

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public List<ShowtimeWithMovieInfo> getAllShowtimesWithMovieInfo() {
        List<Showtime> showtimes = showtimeRepository.findAll();
        return showtimes.stream()
                .map(this::convertToShowtimeWithMovieInfo)
                .collect(Collectors.toList());
    }

    public Showtime getShowtimeById(String id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", id));
    }

    public List<Showtime> getShowtimesByMovieId(String movieId) {
        // Verify movie exists
        movieService.getMovieById(movieId);
        return showtimeRepository.findByMovieIdAndActiveTrue(movieId);
    }

    public List<Showtime> getUpcomingShowtimesByMovieId(String movieId) {
        // Verify movie exists
        movieService.getMovieById(movieId);
        return showtimeRepository.findByMovieIdAndStartTimeAfterAndActiveTrue(movieId, LocalDateTime.now());
    }

    public Showtime createShowtime(Showtime showtime) {
        // Verify movie exists
        movieService.getMovieById(showtime.getMovieId());

        // Check for conflicts
        if (hasScheduleConflict(showtime)) {
            throw new IllegalArgumentException(
                    "Schedule conflict: Another showtime is already scheduled for this screen at this time");
        }

        return showtimeRepository.save(showtime);
    }

    public Showtime updateShowtime(String id, Showtime showtimeDetails) {
        Showtime showtime = getShowtimeById(id);

        // Verify movie exists if movieId is being changed
        if (!showtime.getMovieId().equals(showtimeDetails.getMovieId())) {
            movieService.getMovieById(showtimeDetails.getMovieId());
        }

        showtime.setMovieId(showtimeDetails.getMovieId());
        showtime.setStartTime(showtimeDetails.getStartTime());
        showtime.setEndTime(showtimeDetails.getEndTime());
        showtime.setScreenNumber(showtimeDetails.getScreenNumber());
        showtime.setTotalSeats(showtimeDetails.getTotalSeats());
        showtime.setTicketPrice(showtimeDetails.getTicketPrice());

        // Check for conflicts with the updated schedule
        if (hasScheduleConflictExcluding(showtime, id)) {
            throw new IllegalArgumentException(
                    "Schedule conflict: Another showtime is already scheduled for this screen at this time");
        }

        return showtimeRepository.save(showtime);
    }

    public void deleteShowtime(String id) {
        Showtime showtime = getShowtimeById(id);
        showtimeRepository.delete(showtime);
    }

    public Showtime deactivateShowtime(String id) {
        Showtime showtime = getShowtimeById(id);
        showtime.setActive(false);
        return showtimeRepository.save(showtime);
    }

    public Showtime activateShowtime(String id) {
        Showtime showtime = getShowtimeById(id);
        showtime.setActive(true);
        return showtimeRepository.save(showtime);
    }

    private boolean hasScheduleConflict(Showtime newShowtime) {
        List<Showtime> conflictingShowtimes = showtimeRepository.findByScreenNumberAndStartTimeBetween(
                newShowtime.getScreenNumber(),
                newShowtime.getStartTime().minusMinutes(30), // Buffer time
                newShowtime.getEndTime().plusMinutes(30) // Buffer time
        );

        return !conflictingShowtimes.isEmpty();
    }

    private boolean hasScheduleConflictExcluding(Showtime showtime, String excludeId) {
        List<Showtime> conflictingShowtimes = showtimeRepository.findByScreenNumberAndStartTimeBetween(
                showtime.getScreenNumber(),
                showtime.getStartTime().minusMinutes(30), // Buffer time
                showtime.getEndTime().plusMinutes(30) // Buffer time
        );

        // Remove the current showtime from conflicts
        conflictingShowtimes.removeIf(s -> s.getId().equals(excludeId));

        return !conflictingShowtimes.isEmpty();
    }

    private ShowtimeWithMovieInfo convertToShowtimeWithMovieInfo(Showtime showtime) {
        String movieTitle = "Unknown Movie";
        try {
            Movie movie = movieService.getMovieById(showtime.getMovieId());
            movieTitle = movie.getTitle();
        } catch (Exception e) {
            // If movie not found, keep default title
        }

        return new ShowtimeWithMovieInfo(
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
                showtime.getUpdatedAt()
        );
    }
}
