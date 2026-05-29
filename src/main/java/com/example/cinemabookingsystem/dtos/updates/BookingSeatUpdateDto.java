package com.example.cinemabookingsystem.dtos.updates;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingSeatUpdateDto {
    @Positive(message = "Booking id must be positive")
    private Long bookingId;

    @Positive(message = "Seat id must be positive")
    private Long seatId;

    @PositiveOrZero(message = "Price must be zero or greater")
    private BigDecimal price;
}
