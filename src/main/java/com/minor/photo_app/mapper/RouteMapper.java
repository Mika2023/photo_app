package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.request.RouteRequest;
import com.minor.photo_app.dto.response.CoordinateResponse;
import com.minor.photo_app.dto.response.RouteResponse;
import com.minor.photo_app.dto.response.RouteShortResponse;
import com.minor.photo_app.dto.response.mapsResponse.ResultResponse;
import com.minor.photo_app.entity.Route;
import org.locationtech.jts.geom.LineString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Arrays;
import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RouteMapper {
    Route toEntity(RouteRequest routeRequest);

    @Mapping(target = "distanceMeters", source = "totalDistance")
    @Mapping(target = "estimatedTimeMinutes", source = "totalDuration", qualifiedByName = "convertDurationToMinutes")
    void updateEntity(ResultResponse resultResponse, @MappingTarget Route route);

    @Mapping(target = "totalDistance", source = "distanceMeters")
    @Mapping(target = "totalDuration", source = "estimatedTimeMinutes", qualifiedByName = "convertDurationToUi")
    @Mapping(target = "path", source = "path", qualifiedByName = "convertLineStringToCoordinatesResponse")
    RouteResponse toDto(Route route);

    RouteShortResponse toShortDto(Route route);

    @Named("convertDurationToMinutes")
    default Integer convertDurationToMinutes(Integer duration) {
        return duration / 60;
    }

    @Named("convertDurationToUi")
    default String convertDurationToUi(Integer duration) {
        Integer hours = duration / 60;
        Integer minutes = duration % 60;

        if (hours.equals(0)) {
            return String.format("%d мин", minutes);
        }
        return String.format("%d ч %d мин", hours, minutes);
    }

    @Named("convertLineStringToCoordinatesResponse")
    default List<CoordinateResponse> convertLineStringToCoordinates(LineString lineString) {
        if (lineString == null) {
            return List.of();
        }

        return Arrays.stream(lineString.getCoordinates())
                .map(coordinate -> {
                    CoordinateResponse response = new CoordinateResponse();
                    response.setLatitude(coordinate.y);
                    response.setLongitude(coordinate.x);
                    return response;
                })
                .toList();
    }
}
