package com.minor.photo_app.controller;

import com.minor.photo_app.dto.PointDto;
import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.UserLocationRequest;
import com.minor.photo_app.dto.response.UserLocationResponse;
import com.minor.photo_app.service.UserLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-location")
@RequiredArgsConstructor
public class UserLocationController {

    private final UserLocationService userLocationService;

    @PostMapping("/update-location")
    public UserLocationResponse updateLocation(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody @Valid UserLocationRequest request) {
        return userLocationService.updateUserLocation(userPrincipal, request);
    }

    @GetMapping
    public PointDto getLocation(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userLocationService.getUserLocationPointDto(userPrincipal);
    }
}
