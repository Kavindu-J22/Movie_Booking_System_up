package com.moviebooking.repository;

import com.moviebooking.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    
    List<Booking> findByUserId(String userId);
    
    List<Booking> findByShowtimeId(String showtimeId);
    
    List<Booking> findByShowtimeIdAndStatusIn(String showtimeId, List<Booking.BookingStatus> statuses);
    
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    List<Booking> findByBookingReference(String bookingReference);
}
