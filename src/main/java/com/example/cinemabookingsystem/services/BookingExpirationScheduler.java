package com.example.cinemabookingsystem.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingExpirationScheduler {
    private final BookingService bookingService;

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredPendingBookings() {
        bookingService.cancelExpiredPendingBookings();
    }
}
