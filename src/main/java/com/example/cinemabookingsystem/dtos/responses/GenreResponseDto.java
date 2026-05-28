package com.example.cinemabookingsystem.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GenreResponseDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
