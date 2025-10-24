package com.moviebooking.repository;

import com.moviebooking.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    
    Optional<Payment> findByBookingId(String bookingId);
    
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    List<Payment> findByProcessedAtBetween(LocalDateTime start, LocalDateTime end);
    
    List<Payment> findByTransactionId(String transactionId);
}
