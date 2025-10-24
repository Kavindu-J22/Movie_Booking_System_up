package com.moviebooking.repository;

import com.moviebooking.model.Showtime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends MongoRepository<Showtime, String> {
    
    List<Showtime> findByMovieIdAndActiveTrue(String movieId);
    
    List<Showtime> findByMovieIdAndStartTimeAfterAndActiveTrue(String movieId, LocalDateTime startTime);
    
    List<Showtime> findByScreenNumberAndStartTimeBetween(Integer screenNumber, LocalDateTime start, LocalDateTime end);
    
    List<Showtime> findByActiveTrue();
}
