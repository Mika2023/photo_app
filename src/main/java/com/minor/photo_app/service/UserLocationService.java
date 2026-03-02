package com.minor.photo_app.service;

import com.minor.photo_app.dto.PointDto;
import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.UserLocationRequest;
import com.minor.photo_app.dto.response.UserLocationResponse;
import com.minor.photo_app.entity.User;
import com.minor.photo_app.entity.UserLocation;
import com.minor.photo_app.mapper.UserLocationMapper;
import com.minor.photo_app.repository.UserLocationRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserLocationService {

    private final UserLocationRepository userLocationRepository;
    private final UserService userService;
    private final UserLocationMapper userLocationMapper;

    @Transactional
    public UserLocationResponse updateUserLocation(UserPrincipal userPrincipal, UserLocationRequest request) {
        Long userId = userPrincipal.getId();
        User user = userService.getUserByPrincipal(userPrincipal);

        Point location = new GeometryFactory(new PrecisionModel(), 4326)
                .createPoint(new Coordinate(request.getLongitude(), request.getLatitude()));

        UserLocation userLocation = userLocationRepository.findByUserId(userId)
                .orElseGet(() ->
                        new UserLocation()
                                .setUser(user)
                                .setLocation(location)
                );
        userLocation.setLocation(location);

        UserLocation savedUserLocation = userLocationRepository.save(userLocation);
        return userLocationMapper.toUserLocationResponse(savedUserLocation);
    }

    @Transactional(readOnly = true)
    public Point getUserLocationPoint(UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        User user = userService.getUserByPrincipal(userPrincipal);

        Point location = new GeometryFactory(new PrecisionModel(), 4326)
                .createPoint(new Coordinate(37.621467, 55.754646));

        UserLocation userLocation = userLocationRepository.findByUserId(userId)
                .orElseGet(() ->
                        new UserLocation()
                                .setUser(user)
                                .setLocation(location)
                );
        return userLocation.getLocation();
    }

    @Transactional(readOnly = true)
    public PointDto getUserLocationPointDto(UserPrincipal userPrincipal) {
        Point location = getUserLocationPoint(userPrincipal);
        Double lon = location.getX();
        Double lat = location.getY();
        return new PointDto(lon, lat);
    }
}
