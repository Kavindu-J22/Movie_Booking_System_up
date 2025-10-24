package com.moviebooking.dto.payment;

import com.moviebooking.model.Payment;

import java.time.LocalDateTime;

public class PaymentResponse {

    private String paymentId;
    private String bookingId;
    private Double amount;
    private Payment.PaymentStatus status;
    private String transactionId;
    private String cardNumber;
    private LocalDateTime processedAt;
    private String failureReason;

    // Constructors
    public PaymentResponse() {}

    public PaymentResponse(Payment payment) {
        this.paymentId = payment.getId();
        this.bookingId = payment.getBookingId();
        this.amount = payment.getAmount();
        this.status = payment.getStatus();
        this.transactionId = payment.getTransactionId();
        this.cardNumber = payment.getCardNumber();
        this.processedAt = payment.getProcessedAt();
        this.failureReason = payment.getFailureReason();
    }

    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Payment.PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(Payment.PaymentStatus status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
