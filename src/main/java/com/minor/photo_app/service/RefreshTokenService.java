package com.minor.photo_app.service;

import com.minor.photo_app.entity.RefreshToken;
import com.minor.photo_app.entity.User;
import com.minor.photo_app.repository.RefreshTokenRepository;
import com.minor.photo_app.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthorizationDeniedException("Токен не найден"));

        refreshTokenRepository.delete(token);
    }

    public void deleteAllRefreshTokensByUser(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }

    @Transactional
    public String createNewToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthorizationDeniedException("Токен не найден"));

        if (!isNotExpired(token)) {
            refreshTokenRepository.delete(token);
            throw new AuthorizationDeniedException("Токен истек");
        }

        refreshTokenRepository.delete(token);

        String newTokenString = jwtService.generateRefreshToken(token.getUser());
        RefreshToken newToken = new RefreshToken()
                .setUser(token.getUser())
                .setToken(newTokenString)
                .setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));

        refreshTokenRepository.save(newToken);
        return newTokenString;
    }

    public void saveUserRefreshToken(User user, String refreshToken) {
        RefreshToken token = new RefreshToken()
                .setToken(refreshToken)
                .setUser(user)
                .setExpiresAt(jwtService.extractExpiration(refreshToken));

        refreshTokenRepository.save(token);
    }

    public User getUserByToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthorizationDeniedException("Токен не найден"));

        if (!isNotExpired(token)) {
            refreshTokenRepository.delete(token);
            throw new AuthorizationDeniedException("Токен истек");
        }

        return token.getUser();
    }

    private boolean isNotExpired(RefreshToken token) {
        return Instant.now().isBefore(token.getExpiresAt());
    }
}
