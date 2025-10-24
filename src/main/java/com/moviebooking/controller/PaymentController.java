package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.payment.PaymentRequest;
import com.moviebooking.dto.payment.PaymentResponse;
import com.moviebooking.model.Payment;
import com.moviebooking.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{bookingId}/pay")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            @PathVariable String bookingId,
            @Valid @RequestBody PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = paymentService.processPayment(bookingId, paymentRequest);
        return ResponseEntity.ok(ApiResponse.success("Payment processed successfully", paymentResponse));
    }

    @GetMapping("/{bookingId}/payment")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentStatus(@PathVariable String bookingId) {
        PaymentResponse paymentResponse = paymentService.getPaymentByBookingId(bookingId);
        return ResponseEntity.ok(ApiResponse.success("Payment status retrieved successfully", paymentResponse));
    }
}

@RestController
@RequestMapping("/admin/payments")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
class AdminPaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Payment>>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", payments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Payment>> getPaymentById(@PathVariable String id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", payment));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByStatus(@PathVariable Payment.PaymentStatus status) {
        List<Payment> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", payments));
    }

    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Payment> payments = paymentService.getPaymentsByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", payments));
    }
}
