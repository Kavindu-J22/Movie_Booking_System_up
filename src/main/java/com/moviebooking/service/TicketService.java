package com.moviebooking.service;

import com.google.zxing.WriterException;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Booking;
import com.moviebooking.model.Ticket;
import com.moviebooking.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private QRCodeService qrCodeService;

    public Ticket generateTicket(String bookingId) throws WriterException, IOException {
        // Verify booking exists and is confirmed
        Booking booking = bookingService.getBookingById(bookingId);
        
        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Ticket can only be generated for confirmed bookings");
        }

        // Check if ticket already exists
        Optional<Ticket> existingTicket = ticketRepository.findByBookingId(bookingId);
        if (existingTicket.isPresent()) {
            return existingTicket.get();
        }

        // Create new ticket
        Ticket ticket = new Ticket(bookingId);
        
        // Generate QR code image
        String qrCodeBase64 = qrCodeService.generateQRCodeBase64(ticket.getQrCodeData());
        ticket.setQrCodeImageBase64(qrCodeBase64);

        return ticketRepository.save(ticket);
    }

    public Ticket getTicketByBookingId(String bookingId) {
        return ticketRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "bookingId", bookingId));
    }

    public Ticket getTicketById(String id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));
    }

    public Ticket validateTicket(String qrCodeData) {
        // Validate QR code format
        if (!qrCodeService.validateQRCodeData(qrCodeData)) {
            throw new IllegalArgumentException("Invalid QR code format");
        }

        // Find ticket by QR code data
        Ticket ticket = ticketRepository.findByQrCodeData(qrCodeData)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "qrCodeData", qrCodeData));

        // Check if ticket is still valid
        if (!ticket.isValid()) {
            throw new IllegalArgumentException("Ticket is no longer valid");
        }

        // Verify associated booking is still confirmed
        Booking booking = bookingService.getBookingById(ticket.getBookingId());
        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Associated booking is not confirmed");
        }

        return ticket;
    }

    public void invalidateTicket(String ticketId) {
        Ticket ticket = getTicketById(ticketId);
        ticket.setValid(false);
        ticketRepository.save(ticket);
    }

    public void invalidateTicketByBookingId(String bookingId) {
        Optional<Ticket> ticketOpt = ticketRepository.findByBookingId(bookingId);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            ticket.setValid(false);
            ticketRepository.save(ticket);
        }
    }

    // This method should be called automatically when a payment is successful
    public void handlePaymentSuccess(String bookingId) {
        try {
            generateTicket(bookingId);
        } catch (WriterException | IOException e) {
            // Log error but don't fail the payment process
            System.err.println("Failed to generate ticket for booking " + bookingId + ": " + e.getMessage());
        }
    }
}
