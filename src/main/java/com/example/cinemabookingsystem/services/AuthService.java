package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.dtos.requests.LoginRequestDto;
import com.example.cinemabookingsystem.dtos.requests.RegisterRequestDto;
import com.example.cinemabookingsystem.dtos.responses.AuthResponseDto;
import com.example.cinemabookingsystem.dtos.responses.UserAuthResponseDto;
import com.example.cinemabookingsystem.entity.User;
import com.example.cinemabookingsystem.entity.UserRole;
import com.example.cinemabookingsystem.exceptions.BadRequestException;
import com.example.cinemabookingsystem.exceptions.ResourceNotFoundException;
import com.example.cinemabookingsystem.exceptions.UnauthorizedException;
import com.example.cinemabookingsystem.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordPolicyService passwordPolicyService;
    private final RefreshTokenCookieService refreshTokenCookieService;

    public AuthResponseDto login(LoginRequestDto request, HttpServletResponse response) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new BadRequestException(
                    "Missing email or password. (Hint: check if @RequestBody is missing in your controller)");
        }

        String email = request.getEmail().trim().toLowerCase();

        var user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        return createAuthSession(user, response);
    }

    public AuthResponseDto register(RegisterRequestDto request, HttpServletResponse response) {
        if (request.getEmail() == null || request.getPassword() == null || request.getName() == null) {
            throw new BadRequestException(
                    "Name, email, and password are required. (Hint: check if @RequestBody is missing in your controller)");
        }

        String email = request.getEmail().trim().toLowerCase();
        passwordPolicyService.validateStrongPassword(request.getPassword());

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email is already registered");
        }

        var user = new User();
        user.setName(request.getName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone() != null ? request.getPhone() : "");
        user.setRole(UserRole.customer);

        return createAuthSession(userRepository.saveAndFlush(user), response);
    }

    public String logout(String rawRefreshToken, HttpServletResponse response) {
        if (rawRefreshToken != null
                && (!jwtService.isValid(rawRefreshToken) || !jwtService.isRefreshToken(rawRefreshToken))) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        if (rawRefreshToken != null) {
            refreshTokenService.revokeRefreshToken(rawRefreshToken);
        }
        refreshTokenCookieService.clearRefreshTokenCookie(response);
        return "Logged out successfully";
    }

    public AuthResponseDto refresh(String rawRefreshToken, HttpServletResponse response) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw new UnauthorizedException("Refresh token cookie is missing");
        }

        if (!jwtService.isValid(rawRefreshToken) || !jwtService.isRefreshToken(rawRefreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        var storedToken = refreshTokenService.validateRefreshToken(rawRefreshToken);
        String email = jwtService.getSubject(rawRefreshToken);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (!storedToken.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        refreshTokenService.rotateRefreshToken(
                storedToken,
                newRefreshToken,
                LocalDateTime.now().plusSeconds(jwtService.getRefreshTokenExpirationSeconds()));
        refreshTokenCookieService.addRefreshTokenCookie(response, newRefreshToken);

        return toAuthResponse(user, accessToken);
    }

    public String logoutAll(String email, HttpServletResponse response) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        refreshTokenService.revokeAllForUser(user.getId());
        refreshTokenCookieService.clearRefreshTokenCookie(response);
        return "Logged out from all devices successfully";
    }

    @Transactional(readOnly = true)
    public UserAuthResponseDto me(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toUserAuthResponse(user);
    }

    private AuthResponseDto createAuthSession(User user, HttpServletResponse response) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        refreshTokenService.createRefreshToken(
                user,
                refreshToken,
                LocalDateTime.now().plusSeconds(jwtService.getRefreshTokenExpirationSeconds()));
        refreshTokenCookieService.addRefreshTokenCookie(response, refreshToken);

        return toAuthResponse(user, accessToken);
    }

    private AuthResponseDto toAuthResponse(User user, String accessToken) {
        return new AuthResponseDto(
                "Bearer",
                accessToken,
                jwtService.getAccessTokenExpirationSeconds(),
                jwtService.getRefreshTokenExpirationSeconds(),
                toUserAuthResponse(user));
    }

    private UserAuthResponseDto toUserAuthResponse(User user) {
        return new UserAuthResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                SecurityRoleUtils.responseRole(user.getRole()));
    }
}
