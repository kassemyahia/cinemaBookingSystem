package com.example.cinemabookingsystem.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShowResponseDto {
    private Long id;
    private MovieResponseDto movie;
    private HallResponseDto hall;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal basePrice;
    private BigDecimal vipPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
