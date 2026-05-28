package com.example.cinemabookingsystem.dtos.updates;

import lombok.Data;

@Data
public class MovieGenreUpdateDto {
    private Long movieId;
    private Long genreId;
}
