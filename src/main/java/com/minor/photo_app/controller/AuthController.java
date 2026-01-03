package com.minor.photo_app.controller;

import com.minor.photo_app.dto.request.UserLoginRequest;
import com.minor.photo_app.dto.request.UserRegistrationRequest;
import com.minor.photo_app.dto.response.AuthTokenResponse;
import com.minor.photo_app.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthTokenResponse registrationUser(@RequestBody @Valid UserRegistrationRequest request) {
        return authService.authorizeUser(request);
    }

    @PostMapping("/login")
    public AuthTokenResponse loginUser(@RequestBody @Valid UserLoginRequest request) {
        return authService.loginUser(request);
    }

    @PostMapping("/refresh")
    public AuthTokenResponse refreshToken(@RequestParam @NotBlank String refreshToken) {
        return authService.refreshAccessToken(refreshToken);
    }

    @DeleteMapping("/logout")
    public void logout(@RequestParam @NotBlank String refreshToken) {
        authService.logout(refreshToken);
    }

    @DeleteMapping("/logoutAll")
    public void logoutAll(@RequestParam @NotBlank String refreshToken) {
        authService.logoutAll(refreshToken);
    }
}
