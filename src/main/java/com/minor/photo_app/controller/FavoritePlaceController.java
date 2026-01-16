package com.minor.photo_app.controller;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.response.PlaceCardResponse;
import com.minor.photo_app.service.FavoritePlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("favorite-places")
public class FavoritePlaceController {
    private final FavoritePlaceService favoritePlaceService;

    @GetMapping
    public Slice<PlaceCardResponse> getFavoritePlaces(@AuthenticationPrincipal UserPrincipal user) {
        return favoritePlaceService.getFavoritePlacesByUser(user);
    }

    @PostMapping("/{placeId}")
    public void createOrDeleteFavoritePlace(@PathVariable Long placeId, @AuthenticationPrincipal UserPrincipal user) {
        favoritePlaceService.createOrDeleteFavoritePlace(user, placeId);
    }
}
