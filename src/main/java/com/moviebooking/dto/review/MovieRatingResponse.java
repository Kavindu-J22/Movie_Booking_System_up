package com.moviebooking.dto.review;

public class MovieRatingResponse {

    private String movieId;
    private double averageRating;
    private long totalReviews;
    private long[] ratingDistribution; // Array of 5 elements for ratings 1-5

    // Constructors
    public MovieRatingResponse() {}

    public MovieRatingResponse(String movieId, double averageRating, long totalReviews, long[] ratingDistribution) {
        this.movieId = movieId;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.ratingDistribution = ratingDistribution;
    }

    // Getters and Setters
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public long[] getRatingDistribution() {
        return ratingDistribution;
    }

    public void setRatingDistribution(long[] ratingDistribution) {
        this.ratingDistribution = ratingDistribution;
    }
}
