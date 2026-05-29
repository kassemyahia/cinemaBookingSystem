package com.example.cinemabookingsystem.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGenreToMovieRequestDto {
    @NotNull(message = "Genre id is required")
    @Positive(message = "Genre id must be positive")
    private Long genreId;
}
