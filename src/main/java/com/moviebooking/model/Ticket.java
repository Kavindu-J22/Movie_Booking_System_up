package com.moviebooking.model;

import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Document(collection = "tickets")
public class Ticket extends BaseEntity {

    @NotBlank(message = "Booking ID is required")
    private String bookingId;

    private String qrCodeData;

    private String qrCodeImageBase64;

    private LocalDateTime generatedAt;

    private boolean isValid = true;

    // Constructors
    public Ticket() {}

    public Ticket(String bookingId) {
        this.bookingId = bookingId;
        this.qrCodeData = generateQRCodeData(bookingId);
        this.generatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public String getQrCodeImageBase64() {
        return qrCodeImageBase64;
    }

    public void setQrCodeImageBase64(String qrCodeImageBase64) {
        this.qrCodeImageBase64 = qrCodeImageBase64;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    private String generateQRCodeData(String bookingId) {
        // Generate QR code data that includes booking ID and timestamp
        return "MOVIE_TICKET:" + bookingId + ":" + System.currentTimeMillis();
    }
}
