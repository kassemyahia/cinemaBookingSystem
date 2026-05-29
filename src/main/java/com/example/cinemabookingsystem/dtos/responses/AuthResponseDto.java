package com.example.cinemabookingsystem.dtos.responses;

public record AuthResponseDto(
        String tokenType,
        String accessToken,
        long expiresIn,
        long refreshExpiresIn,
        UserAuthResponseDto user
) {
}
