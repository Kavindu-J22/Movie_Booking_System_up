package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.model.Ticket;
import com.moviebooking.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Ticket>> getTicketByBookingId(@PathVariable String bookingId) {
        Ticket ticket = ticketService.getTicketByBookingId(bookingId);
        return ResponseEntity.ok(ApiResponse.success("Ticket retrieved successfully", ticket));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Ticket>> getTicketById(@PathVariable String id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ApiResponse.success("Ticket retrieved successfully", ticket));
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Ticket>> validateTicket(@RequestParam String qrCodeData) {
        Ticket ticket = ticketService.validateTicket(qrCodeData);
        return ResponseEntity.ok(ApiResponse.success("Ticket validated successfully", ticket));
    }

    @PutMapping("/{id}/invalidate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> invalidateTicket(@PathVariable String id) {
        ticketService.invalidateTicket(id);
        return ResponseEntity.ok(ApiResponse.success("Ticket invalidated successfully"));
    }
}

@RestController
@RequestMapping("/admin/tickets")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
class AdminTicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/generate/{bookingId}")
    public ResponseEntity<ApiResponse<Ticket>> generateTicket(@PathVariable String bookingId) {
        try {
            Ticket ticket = ticketService.generateTicket(bookingId);
            return ResponseEntity.ok(ApiResponse.success("Ticket generated successfully", ticket));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to generate ticket: " + e.getMessage()));
        }
    }
}
