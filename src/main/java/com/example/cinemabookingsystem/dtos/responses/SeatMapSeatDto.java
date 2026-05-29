package com.example.cinemabookingsystem.dtos.responses;

import java.math.BigDecimal;

public record SeatMapSeatDto(
    Long seatId,
    String rowLabel,
    Integer seatNumber,
    String seatType,
    String status,
    BigDecimal price
) {}
