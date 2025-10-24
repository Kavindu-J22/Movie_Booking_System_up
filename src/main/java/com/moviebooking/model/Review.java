package com.moviebooking.model;

import jakarta.validation.constraints.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
public class Review extends BaseEntity {

    @NotBlank(message = "Movie ID is required")
    private String movieId;

    private String userId; // Optional - for authenticated users

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Reviewer name is required")
    private String reviewerName;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Review text is required")
    @Size(min = 10, max = 1000, message = "Review text must be between 10 and 1000 characters")
    private String reviewText;

    private boolean approved = true; // For moderation if needed

    // Constructors
    public Review() {}

    public Review(String movieId, String userId, String email, String reviewerName, Integer rating, String reviewText) {
        this.movieId = movieId;
        this.userId = userId;
        this.email = email;
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    // Getters and Setters
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
