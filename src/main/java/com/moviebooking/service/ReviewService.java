package com.moviebooking.service;

import com.moviebooking.dto.review.MovieRatingResponse;
import com.moviebooking.dto.review.ReviewRequest;
import com.moviebooking.dto.review.ReviewResponse;
import com.moviebooking.dto.review.ReviewUpdateRequest;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Review;
import com.moviebooking.model.User;
import com.moviebooking.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    public List<ReviewResponse> getReviewsByMovieId(String movieId) {
        // Verify movie exists
        movieService.getMovieById(movieId);
        
        List<Review> reviews = reviewRepository.findByMovieIdAndApprovedTrue(movieId);
        return reviews.stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }

    public ReviewResponse createReview(ReviewRequest request, String userEmail) {
        // Verify movie exists
        movieService.getMovieById(request.getMovieId());

        // Get user if authenticated
        String userId = null;
        if (userEmail != null) {
            try {
                User user = userService.findByEmail(userEmail);
                userId = user.getId();
            } catch (Exception e) {
                // User not found, continue as anonymous review
            }
        }

        // Check if user/email already reviewed this movie
        if (userId != null) {
            Optional<Review> existingReview = reviewRepository.findByMovieIdAndUserId(request.getMovieId(), userId);
            if (existingReview.isPresent()) {
                throw new IllegalArgumentException("You have already reviewed this movie. You can update your existing review.");
            }
        } else {
            Optional<Review> existingReview = reviewRepository.findByMovieIdAndEmail(request.getMovieId(), request.getEmail());
            if (existingReview.isPresent()) {
                throw new IllegalArgumentException("This email has already been used to review this movie.");
            }
        }

        // Create new review
        Review review = new Review(
                request.getMovieId(),
                userId,
                request.getEmail(),
                request.getReviewerName(),
                request.getRating(),
                request.getReviewText()
        );

        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        return new ReviewResponse(savedReview);
    }

    public ReviewResponse updateReview(String reviewId, ReviewUpdateRequest request, String userEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        // Check if user has permission to update this review
        if (userEmail != null) {
            try {
                User user = userService.findByEmail(userEmail);
                if (!review.getUserId().equals(user.getId())) {
                    throw new IllegalArgumentException("You can only update your own reviews");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("You can only update your own reviews");
            }
        } else {
            throw new IllegalArgumentException("Authentication required to update reviews");
        }

        // Update review
        review.setRating(request.getRating());
        review.setReviewText(request.getReviewText());
        review.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);
        return new ReviewResponse(updatedReview);
    }

    public void deleteReview(String reviewId, String userEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        // Check if user has permission to delete this review
        boolean canDelete = false;
        
        if (userEmail != null) {
            try {
                User user = userService.findByEmail(userEmail);
                // User can delete their own review or admin can delete any review
                if (review.getUserId() != null && review.getUserId().equals(user.getId())) {
                    canDelete = true;
                } else if (user.getRole().name().equals("ROLE_ADMIN")) {
                    canDelete = true;
                }
            } catch (Exception e) {
                // User not found
            }
        }

        if (!canDelete) {
            throw new IllegalArgumentException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    public ReviewResponse getReviewById(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        return new ReviewResponse(review);
    }

    public MovieRatingResponse getMovieRating(String movieId) {
        // Verify movie exists
        movieService.getMovieById(movieId);

        List<Review> reviews = reviewRepository.findByMovieIdAndApprovedTrue(movieId);
        
        if (reviews.isEmpty()) {
            return new MovieRatingResponse(movieId, 0.0, 0, new long[5]);
        }

        // Calculate average rating
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        // Calculate rating distribution
        long[] distribution = new long[5];
        for (Review review : reviews) {
            distribution[review.getRating() - 1]++;
        }

        return new MovieRatingResponse(movieId, averageRating, reviews.size(), distribution);
    }

    public List<ReviewResponse> getReviewsByUserId(String userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }

    public ReviewResponse approveReview(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        
        review.setApproved(true);
        review.setUpdatedAt(LocalDateTime.now());
        
        Review updatedReview = reviewRepository.save(review);
        return new ReviewResponse(updatedReview);
    }

    public ReviewResponse rejectReview(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        
        review.setApproved(false);
        review.setUpdatedAt(LocalDateTime.now());
        
        Review updatedReview = reviewRepository.save(review);
        return new ReviewResponse(updatedReview);
    }
}
