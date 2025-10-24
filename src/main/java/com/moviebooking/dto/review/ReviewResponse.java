package com.moviebooking.dto.review;

import com.moviebooking.model.Review;

import java.time.LocalDateTime;

public class ReviewResponse {

    private String id;
    private String movieId;
    private String userId;
    private String email;
    private String reviewerName;
    private Integer rating;
    private String reviewText;
    private boolean approved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ReviewResponse() {}

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.movieId = review.getMovieId();
        this.userId = review.getUserId();
        this.email = review.getEmail();
        this.reviewerName = review.getReviewerName();
        this.rating = review.getRating();
        this.reviewText = review.getReviewText();
        this.approved = review.isApproved();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
