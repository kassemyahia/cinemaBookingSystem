package com.example.cinemabookingsystem.dtos.requests;

import com.example.cinemabookingsystem.entity.RowLabel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SeatRequestDto {
    @NotNull(message = "Hall id is required")
    private Long hallId;

    @NotNull(message = "Row label is required")
    private RowLabel rowLabel;

    @NotNull(message = "Seat number is required")
    @Positive(message = "Seat number must be greater than zero")
    private Long seatNumber;

    @NotNull(message = "VIP value is required")
    private Boolean isVip;
}
