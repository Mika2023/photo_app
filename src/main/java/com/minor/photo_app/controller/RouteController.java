package com.minor.photo_app.controller;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.RouteRequest;
import com.minor.photo_app.dto.request.RouteUpdateRequest;
import com.minor.photo_app.dto.response.RouteResponse;
import com.minor.photo_app.dto.response.RouteShortResponse;
import com.minor.photo_app.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("routes")
public class RouteController {
    private final RouteService routeService;

    @PostMapping
    public RouteResponse createRoute(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid RouteRequest routeRequest
    ) {
        return routeService.createRoute(routeRequest, userPrincipal);
    }

    @GetMapping
    public Slice<RouteShortResponse> getRoutes(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        return routeService.getRoutesByUser(userPrincipal, page, size);
    }

    @PostMapping("/{routeId}")
    public RouteResponse updateRoute(@RequestBody @Valid RouteUpdateRequest request, @PathVariable("routeId") Long routeId) {
        return routeService.updateRoute(routeId, request);
    }

    @GetMapping("/{routeId}")
    public RouteResponse getRoute(@PathVariable("routeId") Long routeId) {
        return routeService.getRouteById(routeId);
    }
}
