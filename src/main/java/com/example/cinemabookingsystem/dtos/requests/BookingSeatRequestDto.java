package com.example.cinemabookingsystem.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingSeatRequestDto {
    @NotNull(message = "Booking id is required")
    @Positive(message = "Booking id must be positive")
    private Long bookingId;

    @NotNull(message = "Seat id is required")
    @Positive(message = "Seat id must be positive")
    private Long seatId;

    @PositiveOrZero(message = "Price must be zero or greater")
    private BigDecimal price;
}
