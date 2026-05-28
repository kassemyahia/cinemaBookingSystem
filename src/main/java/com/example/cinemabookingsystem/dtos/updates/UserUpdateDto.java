package com.example.cinemabookingsystem.dtos.updates;

import com.example.cinemabookingsystem.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email must be less than 150 characters")
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Email must be lowercase")
    private String email;

    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters long")
    private String password;

    @Size(max = 30, message = "Phone must be less than 30 characters")
    private String phone;

    private UserRole role;
}
