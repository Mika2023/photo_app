package com.minor.photo_app.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minor.photo_app.dto.PointDto;
import com.minor.photo_app.dto.request.PlaceCreationRequest;
import com.minor.photo_app.dto.request.PlaceUpdateRequest;
import com.minor.photo_app.dto.response.PlaceCardResponse;
import com.minor.photo_app.dto.response.PlaceResponse;
import com.minor.photo_app.dto.response.PlaceShortResponse;
import com.minor.photo_app.dto.response.mapsResponse.items.MapsPlaceResponse;
import com.minor.photo_app.dto.response.mapsResponse.items.NearestStationsResponse;
import com.minor.photo_app.dto.response.mapsResponse.items.PeriodResponse;
import com.minor.photo_app.dto.response.mapsResponse.items.PointShortResponse;
import com.minor.photo_app.dto.response.mapsResponse.items.WorkingHoursResponse;
import com.minor.photo_app.entity.Photo;
import com.minor.photo_app.entity.Place;
import com.minor.photo_app.enums.TransportType;
import com.minor.photo_app.utils.DayUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {PhotoMapper.class, TagMapper.class, CategoryMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PlaceMapper {

    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "photos", source = "photos")
    @Mapping(target = "isFavorite", source = "place", qualifiedByName = "toFavoritePlace")
    @Mapping(target = "categories", source = "categories")
    @Mapping(target = "location", expression = "java(toPointDto(place.getLocation()))")
    PlaceResponse toResponse(Place place, @Context Set<Long> placeFavoriteIds);

    @Mapping(target = "isFavorite", source = "place", qualifiedByName = "toFavoritePlace")
    @Mapping(target = "mainImageUrl", source = "place", qualifiedByName = "toMainImage")
    PlaceCardResponse toCardResponse(Place place, @Context Set<Long> placeFavoriteIds);

    List<PlaceCardResponse> toCardResponseList(List<Place> places, @Context Set<Long> placeFavoriteIds);

    Set<PlaceCardResponse> toCardResponseSet(Set<Place> places, @Context Set<Long> placeFavoriteIds);

    Set<PlaceResponse> toResponseSet(Set<Place> places);

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "favoritePlaceUsers", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Place toEntity(PlaceCreationRequest request);

    PlaceShortResponse toShortResponse(Place place);
    List<PlaceShortResponse> toShortResponseList(List<Place> places);

    void updatePlace(PlaceUpdateRequest request, @MappingTarget Place place);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "twoGisId", source = "id")
    @Mapping(target = "address", source = "fullAddressName")
    @Mapping(target = "location", source = "point")
    @Mapping(target = "workingHours", source = "schedule", qualifiedByName = "toWorkingHours")
    @Mapping(target = "locationDescription", source = "links.nearestStations", qualifiedByName = "toLocationDescription")
    void updatePlaceFromApi(MapsPlaceResponse responseFromApi, @MappingTarget Place place);

    @Named("toFavoritePlace")
    default Boolean toFavoritePlace(Place place, @Context Set<Long> placeFavoriteIds) {
        return placeFavoriteIds != null && placeFavoriteIds.contains(place.getId());
    }

    @Named("toMainImage")
    default String toMainImage(Place place) {
        if (place.getPhotos() == null || place.getPhotos().isEmpty()) {
            return null;
        }

        return place.getPhotos()
                .stream()
                .filter(p -> Boolean.TRUE.equals(p.getIsMain()))
                .map(Photo::getImageUrl)
                .findFirst()
                .orElseGet(() -> place.getPhotos().iterator().next().getImageUrl());
    }

    @Named("toWorkingHours")
    default Map<String, List<Map<String, String>>> toWorkingHours(Map<String, Object> schedule) {
        if (schedule == null || schedule.isEmpty()) {
            return null;
        }

        Map<String, WorkingHoursResponse> workingHoursResponseMap = parseSchedule(schedule);

        return workingHoursResponseMap.entrySet().stream()
                .filter(entry ->
                        Objects.nonNull(entry.getValue()) && Objects.nonNull(entry.getValue().getWorkingHours()))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toLowerCase(),
                        entry -> {
                            WorkingHoursResponse workingHoursResponse = entry.getValue();
                            return workingHoursResponse.getWorkingHours().stream()
                                    .map(this::convertPeriodToMap)
                                    .toList();
                        }
                ));
    }

    @Named("toLocationDescription")
    default Map<String, List<String>> toLocationDescription(List<NearestStationsResponse> nearestStations) {
        if (CollectionUtils.isEmpty(nearestStations)) {
            return null;
        }

        Map<String, List<String>> locationDescription = new HashMap<>();
        for (NearestStationsResponse nearestStationsResponse : nearestStations) {
            String stationName = nearestStationsResponse.getName();
            List<String> routeTypes = nearestStationsResponse.getRouteTypes();

            if (StringUtils.isBlank(stationName) || CollectionUtils.isEmpty(routeTypes)) {
                continue;
            }

            routeTypes.stream()
                    .map(TransportType::getRussianNameByType)
                    .filter(Objects::nonNull)
                    .forEach(routeType -> {
                        List<String> stations = locationDescription.computeIfAbsent(routeType, k -> new ArrayList<>());

                        if (!stations.contains(stationName)) {
                            stations.add(stationName);
                        }
                    });
        }
        return locationDescription;
    }

    default Map<String, String> convertPeriodToMap(PeriodResponse period) {
        Map<String, String> periodMap = new HashMap<>();

        periodMap.put("from", period.getFrom());
        periodMap.put("to", period.getTo());
        return periodMap;
    }

    default PointDto toPointDto(Point point) {
        if (point==null) return null;
        return new PointDto(point.getX(), point.getY());
    }

    default Point toPoint(PointShortResponse pointDto) {
        if (pointDto==null) return null;

        Point location = new GeometryFactory(new PrecisionModel(), 4326)
                .createPoint(new Coordinate(pointDto.getLon(), pointDto.getLat()));
        location.setSRID(4326);
        return location;
    }

    default Map<String, WorkingHoursResponse> parseSchedule(Map<String, Object> schedule) {
        if (schedule == null || schedule.isEmpty()) {
            return new HashMap<>();
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, WorkingHoursResponse> result = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : schedule.entrySet()) {
            String day = entry.getKey();
            Object dayData = entry.getValue();

            if (DayUtils.isDayOfWeek(day)) {
                WorkingHoursResponse wh = mapper.convertValue(dayData, WorkingHoursResponse.class);
                result.put(day, wh);
            }
        }

        return result;
    }
}
