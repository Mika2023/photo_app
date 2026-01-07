package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.PointDto;
import com.minor.photo_app.dto.request.PlaceCreationRequest;
import com.minor.photo_app.dto.request.PlaceUpdateRequest;
import com.minor.photo_app.dto.response.PlaceCardResponse;
import com.minor.photo_app.dto.response.PlaceResponse;
import com.minor.photo_app.entity.Photo;
import com.minor.photo_app.entity.Place;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

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

    void updatePlace(PlaceUpdateRequest request, @MappingTarget Place place);

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

    default PointDto toPointDto(Point point) {
        if (point==null) return null;
        return new PointDto(point.getX(), point.getY());
    }
}
