package com.minor.photo_app.controller;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.PhotoCreateRequest;
import com.minor.photo_app.dto.response.PhotoResponse;
import com.minor.photo_app.service.PhotoService;
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
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping
    public Slice<PhotoResponse> getAllPhotos(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return photoService.getPhotosByUser(userPrincipal);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PhotoResponse createPhoto(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @RequestPart("data") @Valid PhotoCreateRequest request,
                                     @RequestPart("file") MultipartFile image) {
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
