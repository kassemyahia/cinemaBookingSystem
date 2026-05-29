package com.example.cinemabookingsystem.controllers;

import com.example.cinemabookingsystem.dtos.requests.UpdateBookingStatusDto;
import com.example.cinemabookingsystem.dtos.responses.BookingResponseDto;
import com.example.cinemabookingsystem.services.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
class AdminBookingController {
    private final BookingService bookingService;

    @PatchMapping("/{id}/status")
    public BookingResponseDto updateBookingStatus(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateBookingStatusDto request) {
        return bookingService.updateBookingStatus(id, request.status());
    }
}
