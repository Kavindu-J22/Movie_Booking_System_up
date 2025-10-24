package com.moviebooking.repository;

import com.moviebooking.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    
    Optional<Ticket> findByBookingId(String bookingId);
    
    Optional<Ticket> findByQrCodeData(String qrCodeData);
}
