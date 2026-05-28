package com.example.cinemabookingsystem.dtos.updates;

import com.example.cinemabookingsystem.entity.AgeRating;
import com.example.cinemabookingsystem.entity.MovieLanguage;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MovieUpdateDto {
    @Size(max = 200, message = "Title must be less than 200 characters")
    private String title;

    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;

    @Positive(message = "Duration must be greater than zero")
    private Long duration;

    private MovieLanguage language;
    private AgeRating ageRating;

    @Size(max = 500, message = "Poster URL must be less than 500 characters")
    private String posterUrl;

    private LocalDate releaseDate;
    private LocalDate startDate;
}
