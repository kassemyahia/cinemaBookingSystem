package com.example.cinemabookingsystem.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HallResponseDto {
    private Long id;
    private String name;
    private Long totalSeats;
    private LocalDateTime createdAt  ;
    private LocalDateTime updatedAt ;
}
