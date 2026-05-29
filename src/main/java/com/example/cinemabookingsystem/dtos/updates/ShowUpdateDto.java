package com.example.cinemabookingsystem.dtos.updates;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShowUpdateDto {
    private Long movieId;
    private Long hallId;

    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @PositiveOrZero(message = "Base price must be zero or greater")
    private BigDecimal basePrice;

    @PositiveOrZero(message = "VIP price must be zero or greater")
    private BigDecimal vipPrice;
}
