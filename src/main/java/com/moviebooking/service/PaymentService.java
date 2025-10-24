package com.moviebooking.service;

import com.moviebooking.dto.payment.PaymentRequest;
import com.moviebooking.dto.payment.PaymentResponse;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Booking;
import com.moviebooking.model.Payment;
import com.moviebooking.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TicketService ticketService;

    public PaymentResponse processPayment(String bookingId, PaymentRequest paymentRequest) {
        // Get the booking
        Booking booking = bookingService.getBookingById(bookingId);

        // Verify booking is in pending payment status
        if (booking.getStatus() != Booking.BookingStatus.PENDING_PAYMENT) {
            throw new IllegalArgumentException("Booking is not in pending payment status");
        }

        // Check if payment already exists for this booking
        Optional<Payment> existingPayment = paymentRepository.findByBookingId(bookingId);
        if (existingPayment.isPresent()) {
            throw new IllegalArgumentException("Payment already processed for this booking");
        }

        // Create payment record
        Payment payment = new Payment(bookingId, booking.getTotalPrice(),
                paymentRequest.getCardNumber(), paymentRequest.getCardHolderName());

        // Simulate payment processing
        PaymentSimulationResult result = simulatePayment(paymentRequest.getCardNumber());

        payment.setStatus(result.isSuccess() ? Payment.PaymentStatus.SUCCESS : Payment.PaymentStatus.FAILED);
        payment.setFailureReason(result.getFailureReason());
        payment.setProcessedAt(LocalDateTime.now());

        // Save payment
        payment = paymentRepository.save(payment);

        // Update booking status based on payment result
        if (result.isSuccess()) {
            bookingService.updateBookingStatus(bookingId, Booking.BookingStatus.CONFIRMED);
            // Generate ticket automatically upon successful payment
            ticketService.handlePaymentSuccess(bookingId);
        }

        return new PaymentResponse(payment);
    }

    public PaymentResponse getPaymentByBookingId(String bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "bookingId", bookingId));
        return new PaymentResponse(payment);
    }

    public Payment getPaymentById(String id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    public List<Payment> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByProcessedAtBetween(start, end);
    }

    private PaymentSimulationResult simulatePayment(String cardNumber) {
        // Simulation Logic:
        // - Cards starting with '4' -> SUCCESS (Visa simulation)
        // - Cards starting with '5' -> SUCCESS (Mastercard simulation)
        // - Cards starting with '3' -> SUCCESS (Amex simulation)
        // - Cards starting with '2' -> FAILED (Insufficient funds)
        // - Cards starting with '1' -> FAILED (Invalid card)
        // - All others -> FAILED (Card declined)

        if (cardNumber == null || cardNumber.length() < 1) {
            return new PaymentSimulationResult(false, "Invalid card number");
        }

        char firstDigit = cardNumber.charAt(0);

        switch (firstDigit) {
            case '4':
                return new PaymentSimulationResult(true, null);
            case '5':
                return new PaymentSimulationResult(true, null);
            case '3':
                return new PaymentSimulationResult(true, null);
            case '2':
                return new PaymentSimulationResult(false, "Insufficient funds");
            case '1':
                return new PaymentSimulationResult(false, "Invalid card number");
            default:
                return new PaymentSimulationResult(false, "Card declined");
        }
    }

    private static class PaymentSimulationResult {
        private final boolean success;
        private final String failureReason;

        public PaymentSimulationResult(boolean success, String failureReason) {
            this.success = success;
            this.failureReason = failureReason;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getFailureReason() {
            return failureReason;
        }
    }
}
