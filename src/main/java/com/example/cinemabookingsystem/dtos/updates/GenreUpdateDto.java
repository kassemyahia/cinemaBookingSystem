package com.example.cinemabookingsystem.dtos.updates;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GenreUpdateDto {
    @Size(max = 100, message = "Genre name must be less than 100 characters")
    private String name;
}
