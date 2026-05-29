package com.example.cinemabookingsystem.dtos.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShowRequestDto {
    @NotNull(message = "Movie id is required")
    private Long movieId;

    @NotNull(message = "Hall id is required")
    private Long hallId;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;


    @NotNull(message = "Base price is required")
    @PositiveOrZero(message = "Base price must be zero or greater")
    private BigDecimal basePrice;

    @NotNull(message = "VIP price is required")
    @PositiveOrZero(message = "VIP price must be zero or greater")
    private BigDecimal vipPrice;
}
