package com.example.cinemabookingsystem.dtos.requests;

import com.example.cinemabookingsystem.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateBookingStatusDto(
        @NotNull(message = "Booking status is required")
        BookingStatus status
) {
}
