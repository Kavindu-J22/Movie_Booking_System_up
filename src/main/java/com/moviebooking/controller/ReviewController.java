package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.review.MovieRatingResponse;
import com.moviebooking.dto.review.ReviewRequest;
import com.moviebooking.dto.review.ReviewResponse;
import com.moviebooking.dto.review.ReviewUpdateRequest;
import com.moviebooking.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Public endpoints - no authentication required

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getMovieReviews(@PathVariable String movieId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByMovieId(movieId);
        return ResponseEntity.ok(ApiResponse.success("Reviews retrieved successfully", reviews));
    }

    @GetMapping("/movie/{movieId}/rating")
    public ResponseEntity<ApiResponse<MovieRatingResponse>> getMovieRating(@PathVariable String movieId) {
        MovieRatingResponse rating = reviewService.getMovieRating(movieId);
        return ResponseEntity.ok(ApiResponse.success("Movie rating retrieved successfully", rating));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewById(@PathVariable String reviewId) {
        ReviewResponse review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(ApiResponse.success("Review retrieved successfully", review));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {
        String userEmail = authentication != null ? authentication.getName() : null;
        ReviewResponse review = reviewService.createReview(request, userEmail);
        return ResponseEntity.ok(ApiResponse.success("Review created successfully", review));
    }

    // Authenticated endpoints

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable String reviewId,
            @Valid @RequestBody ReviewUpdateRequest request,
            Authentication authentication) {
        ReviewResponse review = reviewService.updateReview(reviewId, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Review updated successfully", review));
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable String reviewId,
            Authentication authentication) {
        reviewService.deleteReview(reviewId, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Review deleted successfully", null));
    }

    @GetMapping("/my-reviews")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getUserReviews(Authentication authentication) {
        // This would require getting user ID from email, but for now we'll implement it in admin controller
        return ResponseEntity.ok(ApiResponse.success("Feature not implemented yet", null));
    }
}

// Admin endpoints
@RestController
@RequestMapping("/admin/reviews")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
class AdminReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getAllReviews() {
        List<ReviewResponse> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(ApiResponse.success("All reviews retrieved successfully", reviews));
    }

    @PutMapping("/{reviewId}/approve")
    public ResponseEntity<ApiResponse<ReviewResponse>> approveReview(@PathVariable String reviewId) {
        ReviewResponse review = reviewService.approveReview(reviewId);
        return ResponseEntity.ok(ApiResponse.success("Review approved successfully", review));
    }

    @PutMapping("/{reviewId}/reject")
    public ResponseEntity<ApiResponse<ReviewResponse>> rejectReview(@PathVariable String reviewId) {
        ReviewResponse review = reviewService.rejectReview(reviewId);
        return ResponseEntity.ok(ApiResponse.success("Review rejected successfully", review));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReviewAsAdmin(@PathVariable String reviewId) {
        reviewService.deleteReview(reviewId, "admin@system.com"); // Admin can delete any review
        return ResponseEntity.ok(ApiResponse.success("Review deleted successfully", null));
    }
}
