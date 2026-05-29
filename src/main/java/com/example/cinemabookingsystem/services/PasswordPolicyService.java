package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.regex.Pattern;

@Service
public class PasswordPolicyService {
    private static final Set<String> WEAK_PASSWORDS = Set.of(
            "123456",
            "password",
            "qwerty",
            "111111"
    );

    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern NUMBER = Pattern.compile("\\d");
    private static final Pattern SPECIAL = Pattern.compile("[^A-Za-z0-9]");

    public void validateStrongPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Password is required");
        }

        if (WEAK_PASSWORDS.contains(password.toLowerCase())) {
            throw new BadRequestException("Password is too weak");
        }

        if (password.length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters long");
        }

        if (!UPPERCASE.matcher(password).find()) {
            throw new BadRequestException("Password must contain at least one uppercase letter");
        }

        if (!LOWERCASE.matcher(password).find()) {
            throw new BadRequestException("Password must contain at least one lowercase letter");
        }

        if (!NUMBER.matcher(password).find()) {
            throw new BadRequestException("Password must contain at least one number");
        }

        if (!SPECIAL.matcher(password).find()) {
            throw new BadRequestException("Password must contain at least one special character");
        }
    }
}
