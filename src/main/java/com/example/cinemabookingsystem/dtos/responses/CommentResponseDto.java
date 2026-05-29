package com.example.cinemabookingsystem.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    private Long id;
    private MovieResponseDto movie;
    private UserResponseDto user;
    private String commentText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
