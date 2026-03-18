package com.minor.photo_app.controller;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.UserEditRequest;
import com.minor.photo_app.dto.response.UserProfileInfoDto;
import com.minor.photo_app.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile-info")
    public UserProfileInfoDto getUserProfileInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userService.getProfileInfo(userPrincipal);
    }

    @PostMapping(
            name = "/profile-info",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    encoding = {
                            @Encoding(
                                    name = "request",
                                    contentType = MediaType.APPLICATION_JSON_VALUE
                            ),
                            @Encoding(
                                    name = "image",
                                    contentType = "image/*"
                            )
                    }
            )
    )
    public UserProfileInfoDto editProfileInfo(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart(value = "request", required = false) @Valid UserEditRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return userService.editProfileInfo(userPrincipal, request, image);
    }
}
