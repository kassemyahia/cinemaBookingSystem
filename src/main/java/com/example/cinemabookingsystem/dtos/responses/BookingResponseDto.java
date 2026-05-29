package com.example.cinemabookingsystem.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponseDto {
    private Long bookingId;
    private String bookingReference;
    private Long userId;
    private Long showId;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime paymentDueAt;
    private PaymentResponseDto payment;
    private List<BookingSeatResponseDto> seats;
}
