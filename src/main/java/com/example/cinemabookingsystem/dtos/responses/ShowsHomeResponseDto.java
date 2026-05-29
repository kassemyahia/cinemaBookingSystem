package com.example.cinemabookingsystem.dtos.responses;

import java.util.List;

public record ShowsHomeResponseDto(
        List<ShowResponseDto> currentShows,
        List<ShowResponseDto> upcomingShows
) {}