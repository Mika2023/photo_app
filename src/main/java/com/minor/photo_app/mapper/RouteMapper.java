package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.request.RouteRequest;
import com.minor.photo_app.entity.Route;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RouteMapper {
    Route toEntity(RouteRequest routeRequest);
}
