package com.example.cinemabookingsystem.dtos.updates;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentUpdateDto {

    @Size(max = 2000, message = "Comment text must be less than 2000 characters")
    private String commentText;
}
