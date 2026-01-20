package com.minor.photo_app.service;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.RouteRequest;
import com.minor.photo_app.dto.response.RouteResponse;
import com.minor.photo_app.dto.response.mapsResponse.MapsApiResponse;
import com.minor.photo_app.entity.Place;
import com.minor.photo_app.entity.Route;
import com.minor.photo_app.entity.User;
import com.minor.photo_app.enums.TransportTypeForMapApi;
import com.minor.photo_app.mapper.RouteMapper;
import com.minor.photo_app.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final MapApiService mapApiService;
    private final UserService userService;
    private final PlaceService placeService;
    private final UserLocationService userLocationService;
    private final RouteMapper routeMapper;

    @Transactional
    public RouteResponse createRoute(RouteRequest routeRequest, UserPrincipal userPrincipal) {
        User user = userService.getUserByPrincipal(userPrincipal);

        Route route = routeMapper.toEntity(routeRequest);
        route.setUser(user);

        Place toPlace = placeService.getPlace(routeRequest.getToPlaceId());
        route.setToPlace(toPlace);
        Point toPlaceLocation = toPlace.getLocation();

        Point fromPlaceLocation;
        if (routeRequest.getFromPlaceId() != null && !routeRequest.getFromPlaceId().equals(routeRequest.getToPlaceId())) {
            Place fromPlace = placeService.getPlace(routeRequest.getFromPlaceId());
            route.setFromPlace(fromPlace);
            fromPlaceLocation = fromPlace.getLocation();
        } else {
            fromPlaceLocation = userLocationService.getUserLocationPoint(userPrincipal);
            route.setFromLocation(fromPlaceLocation);
        }
        TransportTypeForMapApi transportType = routeRequest.getTransportType() == null?
                TransportTypeForMapApi.WALKING: routeRequest.getTransportType();

        MapsApiResponse responseFromMaps = mapApiService.buildRoute(
                fromPlaceLocation.getY(),
                fromPlaceLocation.getX(),
                toPlaceLocation.getY(),
                toPlaceLocation.getX(),
                transportType);
        return new RouteResponse();
    }
}
