package com.example.cinemabookingsystem.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HallRequestDto {
    @NotBlank(message = "Hall name is required")
    @Size(max = 100, message = "Hall name must be less than 100 characters")
    private String name;

    @NotNull(message = "Total seats is required")
    @PositiveOrZero(message = "Total seats must be zero or greater")
    private Long totalSeats;
}
