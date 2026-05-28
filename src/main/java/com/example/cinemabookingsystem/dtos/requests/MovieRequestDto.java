package com.example.cinemabookingsystem.dtos.requests;

import com.example.cinemabookingsystem.entity.AgeRating;
import com.example.cinemabookingsystem.entity.MovieLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MovieRequestDto {
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be less than 200 characters")
    private String title;

    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be greater than zero")
    private Long duration;

    @NotNull(message = "Language is required")
    private MovieLanguage language;

    @NotNull(message = "Age rating is required")
    private AgeRating ageRating;

    @Size(max = 500, message = "Poster URL must be less than 500 characters")
    private String posterUrl;

    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;
}
