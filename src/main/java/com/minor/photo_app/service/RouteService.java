package com.minor.photo_app.service;

import com.minor.photo_app.customConverter.DoubleGisConverter;
import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.RouteRequest;
import com.minor.photo_app.dto.request.RouteUpdateRequest;
import com.minor.photo_app.dto.response.RouteResponse;
import com.minor.photo_app.dto.response.RouteShortResponse;
import com.minor.photo_app.dto.response.mapsResponse.MapsApiResponse;
import com.minor.photo_app.dto.response.mapsResponse.ResultResponse;
import com.minor.photo_app.entity.Place;
import com.minor.photo_app.entity.Route;
import com.minor.photo_app.entity.User;
import com.minor.photo_app.enums.TransportTypeForMapApi;
import com.minor.photo_app.exception.NotFoundException;
import com.minor.photo_app.exception.PhotoAppException;
import com.minor.photo_app.mapper.RouteMapper;
import com.minor.photo_app.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final static String DEFAULT_FROM_PLACE_NAME = "Ваше местоположение";

    private final RouteRepository routeRepository;
    private final MapApiService mapApiService;
    private final UserService userService;
    private final PlaceService placeService;
    private final UserLocationService userLocationService;
    private final RouteMapper routeMapper;

    public RouteResponse createRoute(RouteRequest routeRequest, UserPrincipal userPrincipal) {
        User user = userService.getUserByPrincipal(userPrincipal);

        Route route = routeMapper.toEntity(routeRequest);
        route.setUser(user);

        Place toPlace = placeService.getPlace(routeRequest.getToPlaceId());
        route.setToPlace(toPlace);
        Point toPlaceLocation = toPlace.getLocation();

        Point fromPlaceLocation;
        String fromPlaceName;
        if (routeRequest.getFromPlaceId() != null && !routeRequest.getFromPlaceId().equals(routeRequest.getToPlaceId())) {
            Place fromPlace = placeService.getPlace(routeRequest.getFromPlaceId());
            route.setFromPlace(fromPlace);
            fromPlaceLocation = fromPlace.getLocation();
            fromPlaceName = fromPlace.getName();
        } else {
            fromPlaceLocation = userLocationService.getUserLocationPoint(userPrincipal);
            route.setFromLocation(fromPlaceLocation);
            fromPlaceName = DEFAULT_FROM_PLACE_NAME;
        }
        route.setName(routeRequest.getName() == null ?
                createRouteName(toPlace.getName(), fromPlaceName) : routeRequest.getName());

        TransportTypeForMapApi transportType = routeRequest.getTransportType() == null ?
                TransportTypeForMapApi.WALKING: routeRequest.getTransportType();

        parseResponseFromApi(fromPlaceLocation, toPlaceLocation, transportType, route);

        Route savedRoute = routeRepository.save(route);
        return routeMapper.toDto(savedRoute);
    }

    @Transactional
    public RouteResponse updateRoute(Long routeId, RouteUpdateRequest request) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Такого маршрута по id = %s не существует", routeId)
                ));

        if (request.getTransportType() != null) {
            Place toPlace = null;
            if (request.getToPlaceId() != null && !request.getToPlaceId().equals(route.getToPlace().getId())) {
                toPlace = placeService.getPlace(request.getToPlaceId());
            }

            Place fromPlace = null;
            if (request.getFromPlaceId() != null) {
                if (route.getFromPlace() == null || !route.getFromPlace().getId().equals(request.getFromPlaceId())) {
                    fromPlace = placeService.getPlace(request.getFromPlaceId());
                }
            }

            boolean isGeneratedName = checkNameIfGenerated(
                    route.getName(),
                    route.getFromPlace() == null ? DEFAULT_FROM_PLACE_NAME: route.getFromPlace().getName(),
                    route.getToPlace().getName()
            );

            if (Objects.nonNull(toPlace) && Objects.nonNull(fromPlace)) {
                route.setToPlace(toPlace);
                route.setFromPlace(fromPlace);
                route.setFromLocation(null);
                parseResponseFromApi(fromPlace.getLocation(), toPlace.getLocation(), request.getTransportType(), route);
            } else if (Objects.nonNull(toPlace)) {
                route.setToPlace(toPlace);
                parseResponseFromApi(
                        route.getFromPlace() == null ? route.getFromLocation() : route.getFromPlace().getLocation(),
                        toPlace.getLocation(),
                        request.getTransportType(),
                        route
                );
            } else if (Objects.nonNull(fromPlace)) {
                route.setFromPlace(fromPlace);
                route.setFromLocation(null);
                parseResponseFromApi(
                        fromPlace.getLocation(),
                        route.getToPlace().getLocation(),
                        request.getTransportType(),
                        route
                );
            }

            if (isGeneratedName) {
                String newName = createRouteName(
                        route.getToPlace().getName(),
                        route.getFromPlace() == null ? DEFAULT_FROM_PLACE_NAME: route.getFromPlace().getName()
                );
                route.setName(newName);
            }
        }

        if (!StringUtils.isBlank(request.getName())) {
            route.setName(request.getName());
        }
        if (!StringUtils.isBlank(request.getDescription())) {
            route.setDescription(request.getDescription());
        }
        routeRepository.save(route);
        return routeMapper.toDto(route);
    }

    public Slice<RouteShortResponse> getRoutesByUser(UserPrincipal userPrincipal, int page, int size) {
        userService.existsByIdOrElseThrow(userPrincipal.getId());
        Slice<Route> routes = routeRepository.findAllByUserId(userPrincipal.getId(), PageRequest.of(page, size));
        return routes.map(routeMapper::toShortDto);
    }

    public RouteResponse getRouteById(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Такого маршрута по id = %s не существует", routeId)
                ));
        return routeMapper.toDto(route);
    }

    private String createRouteName(String toPlaceName, String fromPlaceName) {
        return fromPlaceName + " -> " + toPlaceName;
    }

    private boolean checkNameIfGenerated(String routeName, String fromPlaceName, String toPlaceName) {
        String generatedName = createRouteName(toPlaceName, fromPlaceName);
        return routeName.equals(generatedName);
    }

    private void parseResponseFromApi(
            Point fromPlaceLocation,
            Point toPlaceLocation,
            TransportTypeForMapApi transportType,
            Route route
    ) {
        MapsApiResponse responseFromMaps = mapApiService.buildRoute(
                fromPlaceLocation.getY(),
                fromPlaceLocation.getX(),
                toPlaceLocation.getY(),
                toPlaceLocation.getX(),
                transportType);

        if (responseFromMaps.getResult() == null || responseFromMaps.getResult().isEmpty()) {
            throw new PhotoAppException(
                    "Ответ от 2ГИС на создание маршрута пришел пустым"
            );
        }
        ResultResponse resultResponse = responseFromMaps.getResult().getFirst();
        routeMapper.updateEntity(resultResponse, route);
        LineString maneuversLineString = DoubleGisConverter.convertManeuversToLineString(resultResponse.getManeuvers());
        route.setPath(maneuversLineString);
    }
}
