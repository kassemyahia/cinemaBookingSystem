package com.example.cinemabookingsystem.dtos.responses;

import com.example.cinemabookingsystem.entity.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
