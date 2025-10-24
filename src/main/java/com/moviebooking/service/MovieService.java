package com.moviebooking.service;

import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Movie;
import com.moviebooking.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<Movie> getAllActiveMovies() {
        return movieRepository.findByActiveTrue();
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(String id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreAndActiveTrue(genre);
    }

    public List<Movie> searchMovies(String title) {
        return movieRepository.findByTitleContainingIgnoreCaseAndActiveTrue(title);
    }

    public Movie createMovie(Movie movie, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            Map<String, Object> uploadResult = cloudinaryService.uploadImage(imageFile);
            movie.setImageUrl((String) uploadResult.get("secure_url"));
            movie.setImagePublicId((String) uploadResult.get("public_id"));
        }
        
        return movieRepository.save(movie);
    }

    public Movie updateMovie(String id, Movie movieDetails, MultipartFile imageFile) throws IOException {
        Movie movie = getMovieById(id);

        movie.setTitle(movieDetails.getTitle());
        movie.setDescription(movieDetails.getDescription());
        movie.setGenre(movieDetails.getGenre());
        movie.setDuration(movieDetails.getDuration());
        movie.setDirector(movieDetails.getDirector());
        movie.setCast(movieDetails.getCast());
        movie.setLanguage(movieDetails.getLanguage());
        movie.setRating(movieDetails.getRating());
        movie.setImdbRating(movieDetails.getImdbRating());
        movie.setTrailerUrl(movieDetails.getTrailerUrl());

        // Handle image update
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if exists
            if (movie.getImagePublicId() != null) {
                try {
                    cloudinaryService.deleteImage(movie.getImagePublicId());
                } catch (IOException e) {
                    // Log error but don't fail the update
                    System.err.println("Failed to delete old image: " + e.getMessage());
                }
            }

            // Upload new image
            Map<String, Object> uploadResult = cloudinaryService.uploadImage(imageFile);
            movie.setImageUrl((String) uploadResult.get("secure_url"));
            movie.setImagePublicId((String) uploadResult.get("public_id"));
        }

        return movieRepository.save(movie);
    }

    public void deleteMovie(String id) throws IOException {
        Movie movie = getMovieById(id);
        
        // Delete image from Cloudinary if exists
        if (movie.getImagePublicId() != null) {
            try {
                cloudinaryService.deleteImage(movie.getImagePublicId());
            } catch (IOException e) {
                // Log error but don't fail the deletion
                System.err.println("Failed to delete image from Cloudinary: " + e.getMessage());
            }
        }
        
        movieRepository.delete(movie);
    }

    public Movie deactivateMovie(String id) {
        Movie movie = getMovieById(id);
        movie.setActive(false);
        return movieRepository.save(movie);
    }

    public Movie activateMovie(String id) {
        Movie movie = getMovieById(id);
        movie.setActive(true);
        return movieRepository.save(movie);
    }
}
