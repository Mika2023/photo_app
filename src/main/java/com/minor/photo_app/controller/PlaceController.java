package com.minor.photo_app.controller;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.filters.PlaceFilter;
import com.minor.photo_app.dto.request.PlaceCreationRequest;
import com.minor.photo_app.dto.request.PlaceSearchRequest;
import com.minor.photo_app.dto.request.PlaceUpdateRequest;
import com.minor.photo_app.dto.response.PlaceCardResponse;
import com.minor.photo_app.dto.response.PlaceResponse;
import com.minor.photo_app.dto.response.PlaceShortResponse;
import com.minor.photo_app.enums.TransportType;
import com.minor.photo_app.service.PlaceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
@Validated
public class PlaceController {
    private final PlaceService placeService;

    @PostMapping("/filters")
    public Slice<PlaceCardResponse> getPlacesByFilter(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                      @RequestBody PlaceFilter placeFilter) {
        return placeService.findPlacesByFilter(userPrincipal, placeFilter);
    }

    @PostMapping("/search")
    public List<PlaceCardResponse> getPlaceBySearch(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @RequestBody @Valid PlaceSearchRequest placeSearchRequest) {
        return placeService.getPlacesBySearch(userPrincipal, placeSearchRequest);
    }

    @GetMapping("/{placeId}/placesNearby")
    public List<PlaceCardResponse> findPlacesNearby(@PathVariable Long placeId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return placeService.findPlacesNearby(placeId, userPrincipal);
    }

    @GetMapping("/filters")
    public Set<String> findAllStationsByTransportType(@RequestParam String transportType) {
        return placeService.getPlacesLocationDescriptionByTransportType(transportType);
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

    @PostMapping("/all")
    public void createPlaceList(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @RequestBody @NotEmpty List<PlaceCreationRequest> requests) {
        placeService.createPlaceList(requests, userPrincipal);
    }

    @DeleteMapping("/{placeId}")
    public void deletePlace(@PathVariable Long placeId) {
        placeService.deletePlace(placeId);
    }

    @DeleteMapping("/{placeId}/categories")
    public void removeCategoryFromPlace(@PathVariable Long placeId, @RequestParam Long categoryId) {
        placeService.removeCategoryFromPlace(placeId, categoryId);
    }

    @GetMapping
    public List<PlaceShortResponse> getAllPlaces() {
        return placeService.getAllPlaces();
    }
}
