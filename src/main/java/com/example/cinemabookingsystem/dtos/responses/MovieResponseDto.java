package com.example.cinemabookingsystem.dtos.responses;

import com.example.cinemabookingsystem.entity.AgeRating;
import com.example.cinemabookingsystem.entity.MovieLanguage;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovieResponseDto {
    private Long id;
    private String title;
    private String description;
    private Long duration;
    private MovieLanguage language;
    private AgeRating ageRating;
    private String posterUrl;
    private LocalDate releaseDate;
    private LocalDate startDate;
    private List<GenreResponseDto> genres;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
