package com.example.cinemabookingsystem.controllers;

import com.example.cinemabookingsystem.dtos.requests.PayBookingRequestDto;
import com.example.cinemabookingsystem.dtos.responses.BookingResponseDto;
import com.example.cinemabookingsystem.services.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/payments")
class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/bookings/{bookingId}/pay")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public BookingResponseDto payBooking(@PathVariable Long bookingId,
                                         @Valid @RequestBody PayBookingRequestDto request) {
        return paymentService.payBooking(bookingId, request);
    }
}
