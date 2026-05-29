package com.example.cinemabookingsystem.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthResponseDto {
    private Long userId;
    private String name;
    private String email;
    private String role;
}
