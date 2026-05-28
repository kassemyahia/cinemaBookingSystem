package com.example.cinemabookingsystem.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GenreRequestDto {
    @NotBlank(message = "Genre name is required")
    @Size(max = 100, message = "Genre name must be less than 100 characters")
    private String name;
}
