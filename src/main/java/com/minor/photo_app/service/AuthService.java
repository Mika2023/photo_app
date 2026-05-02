package com.minor.photo_app.service;

import com.minor.photo_app.dto.request.UserChangePasswordRequest;
import com.minor.photo_app.dto.request.UserLoginRequest;
import com.minor.photo_app.dto.request.UserRegistrationRequest;
import com.minor.photo_app.dto.response.AuthTokenResponse;
import com.minor.photo_app.dto.response.CodeForChangingPassword;
import com.minor.photo_app.entity.User;
import com.minor.photo_app.exception.NotFoundException;
import com.minor.photo_app.mapper.UserMapper;
import com.minor.photo_app.repository.UserRepository;
import com.minor.photo_app.security.JwtService;
import com.minor.photo_app.utils.CodeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;
    private final UserService userService;

    @Transactional
    public AuthTokenResponse authorizeUser(UserRegistrationRequest request) {
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Пользователь с такой почтой уже существует!");
        }

        User userEntity = userMapper.toEntityFromRegistration(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(userEntity);
        return generateAuthTokenAndSave(savedUser);
    }

    public AuthTokenResponse loginUser(UserLoginRequest request) {
        User user = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("Пароль или почта неверные!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Пароль или почта неверные!");
        }

        return generateAuthTokenAndSave(user);
    }

    @Transactional
    public AuthTokenResponse refreshAccessToken(String refreshToken) {
        User user = refreshTokenService.getUserByToken(refreshToken);
        if(!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("Пользователь с таким токеном не найден");
        }

        String newRefreshToken = refreshTokenService.createNewToken(refreshToken);
        String newAccessToken = jwtService.generateToken(user);
        return new AuthTokenResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken);
    }

    @Transactional
    public void logoutAll(String refreshToken) {
        User user = refreshTokenService.getUserByToken(refreshToken);
        if(!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("Пользователь с таким токеном не найден");
        }

        refreshTokenService.deleteAllRefreshTokensByUser(user);
    }

    public CodeForChangingPassword sendCodeForChangingPassword(String toEmail) {
        Long userId = userService.getIdByEmailOrElseThrow(toEmail);

        String verificationCode = CodeUtils.getRandomSixthCode();
        emailService.sendEmail(toEmail, verificationCode);

        return new CodeForChangingPassword()
                .setUserId(userId)
                .setCode(verificationCode);
    }

    @Transactional
    public AuthTokenResponse changePassword(UserChangePasswordRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return generateAuthTokenAndSave(user);
    }

    private AuthTokenResponse generateAuthTokenAndSave(User user) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        refreshTokenService.saveUserRefreshToken(user, refreshToken);

        return new AuthTokenResponse(accessToken, refreshToken);
    }
}
