package com.minor.photo_app.controller;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.response.UserProfileInfoDto;
import com.minor.photo_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile-info")
    public UserProfileInfoDto getUserProfileInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userService.getProfileInfo(userPrincipal);
    }
}
