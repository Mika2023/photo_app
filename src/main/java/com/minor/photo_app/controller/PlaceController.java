package com.minor.photo_app.controller;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.filters.PlaceFilter;
import com.minor.photo_app.dto.request.PlaceCreationRequest;
import com.minor.photo_app.dto.request.PlaceSearchRequest;
import com.minor.photo_app.dto.request.PlaceUpdateRequest;
import com.minor.photo_app.dto.response.PlaceCardResponse;
import com.minor.photo_app.dto.response.PlaceResponse;
import com.minor.photo_app.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @PostMapping("/filters")
    public Slice<PlaceCardResponse> getPlacesByFilter(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                      @RequestBody PlaceFilter placeFilter) {
        return placeService.findPlacesByFilter(userPrincipal, placeFilter);
    }

    @PostMapping("/search")
    public List<PlaceCardResponse> getPlaceBySearch(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @RequestBody PlaceSearchRequest placeSearchRequest) {
        return placeService.getPlacesBySearch(userPrincipal, placeSearchRequest);
    }

    @GetMapping("/{placeId}/placesNearby")
    public List<PlaceCardResponse> findPlacesNearby(@PathVariable Long placeId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return placeService.findPlacesNearby(placeId, userPrincipal);
    }

    @GetMapping("/{placeId}")
    public PlaceResponse getPlaceById(@PathVariable Long placeId,
                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return placeService.getPlaceById(placeId, userPrincipal);
    }

    @PostMapping("/{placeId}")
    public PlaceResponse editPlace(@PathVariable Long placeId,
                                   @AuthenticationPrincipal UserPrincipal userPrincipal,
                                   @RequestBody PlaceUpdateRequest request) {
        return placeService.updatePlace(placeId, userPrincipal, request);
    }

    @PostMapping
    public PlaceResponse createPlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @RequestBody PlaceCreationRequest request) {
        return placeService.createPlace(request, userPrincipal);
    }

    @DeleteMapping("/{placeId}")
    public void deletePlace(@PathVariable Long placeId) {
        placeService.deletePlace(placeId);
    }

    @DeleteMapping("/{placeId}/categories")
    public void removeCategoryFromPlace(@PathVariable Long placeId, @RequestParam Long categoryId) {
        placeService.removeCategoryFromPlace(placeId, categoryId);
    }
}
