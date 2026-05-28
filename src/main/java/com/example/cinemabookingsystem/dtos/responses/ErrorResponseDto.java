package com.example.cinemabookingsystem.dtos.responses;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp
) {
}
