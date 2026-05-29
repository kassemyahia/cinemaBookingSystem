package com.example.cinemabookingsystem.dtos.updates;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HallUpdateDto {
    @Size(max = 100, message = "Hall name must be less than 100 characters")
    private String name;

    @PositiveOrZero(message = "Total seats must be zero or greater")
    private Long totalSeats;
}
