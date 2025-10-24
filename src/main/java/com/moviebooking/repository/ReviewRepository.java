package com.moviebooking.repository;

import com.moviebooking.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    
    List<Review> findByMovieIdAndApprovedTrue(String movieId);
    
    List<Review> findByMovieId(String movieId);
    
    List<Review> findByUserId(String userId);
    
    List<Review> findByEmail(String email);
    
    Optional<Review> findByMovieIdAndUserId(String movieId, String userId);
    
    Optional<Review> findByMovieIdAndEmail(String movieId, String email);
    
    List<Review> findByApproved(boolean approved);
    
    @Query("{ 'movieId': ?0 }")
    List<Review> findReviewsByMovieId(String movieId);
    
    @Query(value = "{ 'movieId': ?0, 'approved': true }", count = true)
    long countByMovieIdAndApprovedTrue(String movieId);
    
    @Query(value = "{ 'movieId': ?0, 'approved': true }", fields = "{ 'rating': 1 }")
    List<Review> findRatingsByMovieId(String movieId);
}
