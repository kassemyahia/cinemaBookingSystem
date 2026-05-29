package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.dtos.requests.BookingSeatRequestDto;
import com.example.cinemabookingsystem.dtos.responses.BookingSeatResponseDto;
import com.example.cinemabookingsystem.dtos.updates.BookingSeatUpdateDto;
import com.example.cinemabookingsystem.exceptions.ResourceNotFoundException;
import com.example.cinemabookingsystem.mappers.BookingSeatMapper;
import com.example.cinemabookingsystem.repositories.BookingRepository;
import com.example.cinemabookingsystem.repositories.BookingSeatRepository;
import com.example.cinemabookingsystem.repositories.SeatRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class BookingSeatService {
    private final BookingSeatRepository bookingSeatRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final BookingSeatMapper bookingSeatMapper;
    private final EntityManager entityManager;

    public List<BookingSeatResponseDto> getBookingSeats() {
        return bookingSeatRepository.findAll().stream()
                .map(bookingSeatMapper::toResponseDto)
                .toList();
    }

    public BookingSeatResponseDto getBookingSeatById(Long id) {
        return bookingSeatRepository.findById(id)
                .map(bookingSeatMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Booking seat not found"));
    }

    public BookingSeatResponseDto createBookingSeat(BookingSeatRequestDto bookingSeatRequest) {
        var booking = bookingRepository.findById(bookingSeatRequest.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        var seat = seatRepository.findById(bookingSeatRequest.getSeatId())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        var bookingSeat = bookingSeatMapper.toEntity(bookingSeatRequest);
        bookingSeat.setBooking(booking);
        bookingSeat.setShow(booking.getShow());
        bookingSeat.setSeat(seat);
        bookingSeat = bookingSeatRepository.saveAndFlush(bookingSeat);
        entityManager.refresh(bookingSeat);
        return bookingSeatMapper.toResponseDto(bookingSeat);
    }

    public BookingSeatResponseDto updateBookingSeat(Long id, BookingSeatUpdateDto bookingSeatUpdate) {
        var bookingSeat = bookingSeatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking seat not found"));
        bookingSeatMapper.updateEntity(bookingSeatUpdate, bookingSeat);

        if (bookingSeatUpdate.getBookingId() != null) {
            var booking = bookingRepository.findById(bookingSeatUpdate.getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
            bookingSeat.setBooking(booking);
            bookingSeat.setShow(booking.getShow());
        }

        if (bookingSeatUpdate.getSeatId() != null) {
            var seat = seatRepository.findById(bookingSeatUpdate.getSeatId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
            bookingSeat.setSeat(seat);
        }

        var savedBookingSeat = bookingSeatRepository.saveAndFlush(bookingSeat);
        entityManager.refresh(savedBookingSeat);
        return bookingSeatMapper.toResponseDto(savedBookingSeat);
    }

    public void deleteBookingSeat(Long id) {
        if (!bookingSeatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking seat not found");
        }

        bookingSeatRepository.deleteById(id);
    }
}
