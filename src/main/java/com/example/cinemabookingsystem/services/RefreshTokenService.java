package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.entity.RefreshToken;
import com.example.cinemabookingsystem.entity.User;
import com.example.cinemabookingsystem.exceptions.UnauthorizedException;
import com.example.cinemabookingsystem.repositories.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(User user, String rawToken, LocalDateTime expiresAt) {
        var refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hashToken(rawToken));
        refreshToken.setExpiresAt(expiresAt);
        return refreshTokenRepository.saveAndFlush(refreshToken);
    }

    @Transactional(readOnly = true)
    public RefreshToken validateRefreshToken(String rawToken) {
        var refreshToken = refreshTokenRepository.findByTokenHash(hashToken(rawToken))
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (refreshToken.isRevoked() || refreshToken.isExpired()) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        return refreshToken;
    }

    public RefreshToken rotateRefreshToken(RefreshToken oldToken, String newRawToken, LocalDateTime newExpiresAt) {
        String newTokenHash = hashToken(newRawToken);
        oldToken.setRevokedAt(LocalDateTime.now());
        oldToken.setReplacedByTokenHash(newTokenHash);

        var newToken = new RefreshToken();
        newToken.setUser(oldToken.getUser());
        newToken.setTokenHash(newTokenHash);
        newToken.setExpiresAt(newExpiresAt);
        refreshTokenRepository.save(oldToken);
        return refreshTokenRepository.saveAndFlush(newToken);
    }

    public void revokeRefreshToken(String rawToken) {
        refreshTokenRepository.findByTokenHash(hashToken(rawToken))
                .ifPresent(refreshToken -> {
                    if (!refreshToken.isRevoked()) {
                        refreshToken.setRevokedAt(LocalDateTime.now());
                    }
                });
    }

    public void revokeAllForUser(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId, LocalDateTime.now());
    }

    public String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }
}
