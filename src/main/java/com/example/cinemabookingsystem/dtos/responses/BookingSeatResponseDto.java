package com.example.cinemabookingsystem.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingSeatResponseDto {
    private Long bookingSeatId;
    private Long seatId;
    private String rowLabel;
    private Integer seatNumber;
    private String seatType;
    private BigDecimal price;
}
