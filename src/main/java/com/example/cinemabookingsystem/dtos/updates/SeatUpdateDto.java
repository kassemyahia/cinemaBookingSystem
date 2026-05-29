package com.example.cinemabookingsystem.dtos.updates;

import com.example.cinemabookingsystem.entity.RowLabel;
import com.example.cinemabookingsystem.entity.SeatType;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SeatUpdateDto {
    private Long hallId;
    private RowLabel rowLabel;

    @Positive(message = "Seat number must be greater than zero")
    private Long seatNumber;

    private SeatType seatType;
}
