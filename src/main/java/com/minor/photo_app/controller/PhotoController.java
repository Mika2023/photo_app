package com.minor.photo_app.controller;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.PhotoCreateRequest;
import com.minor.photo_app.dto.response.PhotoResponse;
import com.minor.photo_app.service.PhotoService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
@Tag(name = "Апи для фоток")
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping
    public Slice<PhotoResponse> getAllPhotos(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return photoService.getPhotosByUser(userPrincipal);
    }

    @PostMapping(
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
    public PhotoResponse createPhoto(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @RequestPart("request") @Valid PhotoCreateRequest request,
                                     @RequestPart("image") MultipartFile image) {
        return photoService.createPhoto(image, request,  userPrincipal);
    }

    @DeleteMapping("/{photoId}")
    public void deletePhoto(@PathVariable("photoId") Long photoId) {
        photoService.deletePhoto(photoId);
    }

    @PostMapping("/{photoId}")
    public PhotoResponse updatePhoto(@PathVariable("photoId") Long photoId, @RequestParam("placeId") Long placeId) {
        return photoService.updatePhotoWithPlace(photoId, placeId);
    }
}
