package com.example.cinemabookingsystem.controllers;

import com.example.cinemabookingsystem.dtos.requests.LoginRequestDto;
import com.example.cinemabookingsystem.dtos.requests.RegisterRequestDto;
import com.example.cinemabookingsystem.dtos.responses.AuthResponseDto;
import com.example.cinemabookingsystem.dtos.responses.UserAuthResponseDto;
import com.example.cinemabookingsystem.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponseDto login(
            @Valid @RequestBody LoginRequestDto request,
            HttpServletResponse response) {
        return authService.login(request, response);
    }

    @PostMapping("/register")
    public AuthResponseDto register(@Valid @RequestBody RegisterRequestDto request,
                                    HttpServletResponse response) {
        return authService.register(request, response);
    }

    @PostMapping("/logout")
    public String logout(@CookieValue(name = "refreshToken", required = false) String refreshToken,
                         HttpServletResponse response) {
        return authService.logout(refreshToken, response);
    }

    @PostMapping("/refresh")
    public AuthResponseDto refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken,
                                   HttpServletResponse response) {
        return authService.refresh(refreshToken, response);
    }

    @PostMapping("/logout-all")
    public String logoutAll(Authentication authentication, HttpServletResponse response) {
        return authService.logoutAll(authentication.getName(), response);
    }

    @GetMapping("/me")
    public UserAuthResponseDto me(Authentication authentication) {
        return authService.me(authentication.getName());
    }
}
