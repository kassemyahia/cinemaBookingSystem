package com.example.cinemabookingsystem.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovieGenreResponseDto {
    private Long id;
    private MovieResponseDto movie;
    private GenreResponseDto genre;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
