package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.response.UserLocationResponse;
import com.minor.photo_app.entity.UserLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserLocationMapper {

    @Mapping(target = "latitude", expression = "java(userLocation.getLocation().getY())")
    @Mapping(target = "longitude", expression = "java(userLocation.getLocation().getX())")
    UserLocationResponse toUserLocationResponse(UserLocation userLocation);
}
