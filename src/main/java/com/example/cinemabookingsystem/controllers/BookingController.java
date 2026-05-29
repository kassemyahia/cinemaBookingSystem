package com.example.cinemabookingsystem.controllers;

import com.example.cinemabookingsystem.dtos.requests.CreateBookingRequestDto;
import com.example.cinemabookingsystem.dtos.responses.BookingResponseDto;
import com.example.cinemabookingsystem.services.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
class BookingController {
    private final BookingService bookingService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<BookingResponseDto> getBookings(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return bookingService.getBookings(pageable);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public Page<BookingResponseDto> getMyBookings(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return bookingService.getMyBookings(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public BookingResponseDto getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody CreateBookingRequestDto bookingRequest,
                                                            UriComponentsBuilder uriBuilder) {
        var booking = bookingService.createBooking(bookingRequest);
        var uri = uriBuilder.path("/bookings/{id}").buildAndExpand(booking.getBookingId()).toUri();
        return ResponseEntity.created(uri).body(booking);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public BookingResponseDto cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
