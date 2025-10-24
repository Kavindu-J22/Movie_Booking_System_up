package com.moviebooking.repository;

import com.moviebooking.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    
    List<Movie> findByActiveTrue();
    
    List<Movie> findByGenreAndActiveTrue(String genre);
    
    List<Movie> findByTitleContainingIgnoreCaseAndActiveTrue(String title);
}
