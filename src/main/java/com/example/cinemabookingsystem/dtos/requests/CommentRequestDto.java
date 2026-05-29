package com.example.cinemabookingsystem.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestDto {
    @NotNull(message = "Movie id is required")
    private Long movieId;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotBlank(message = "Comment text is required")
    @Size(max = 2000, message = "Comment text must be less than 2000 characters")
    private String commentText;
}
