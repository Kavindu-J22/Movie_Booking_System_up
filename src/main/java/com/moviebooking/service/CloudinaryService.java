package com.moviebooking.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Value("${cloudinary.upload-preset}")
    private String uploadPreset;

    public Map<String, Object> uploadImage(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), 
            ObjectUtils.asMap(
                "upload_preset", uploadPreset,
                "resource_type", "image",
                "folder", "movie-posters"
            ));
    }

    public Map<String, Object> deleteImage(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public String extractPublicId(String imageUrl) {
        // Extract public ID from Cloudinary URL
        // Example URL: https://res.cloudinary.com/dfj4ah2m2/image/upload/v1234567890/movie-posters/sample.jpg
        if (imageUrl != null && imageUrl.contains("/")) {
            String[] parts = imageUrl.split("/");
            if (parts.length > 0) {
                String lastPart = parts[parts.length - 1];
                // Remove file extension
                return lastPart.substring(0, lastPart.lastIndexOf('.'));
            }
        }
        return null;
    }
}
