package com.example.cinemabookingsystem.dtos.responses;

import com.example.cinemabookingsystem.entity.RowLabel;
import com.example.cinemabookingsystem.entity.SeatStatus;
import com.example.cinemabookingsystem.entity.SeatType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SeatResponseDto {
    private Long id;
    private Long hallId;
    private RowLabel rowLabel;
    private Long seatNumber;
    private SeatType seatType;
    private SeatStatus status;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
