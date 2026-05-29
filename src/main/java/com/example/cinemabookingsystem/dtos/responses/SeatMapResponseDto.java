package com.example.cinemabookingsystem.dtos.responses;

import java.util.List;

public record SeatMapResponseDto(
                Long showId,
                Long hallId,
                String hallName,
                Long totalSeats,
                Integer rowCount,
                Integer seatsPerRow,
                List<SeatMapSeatDto> seats) {
}
