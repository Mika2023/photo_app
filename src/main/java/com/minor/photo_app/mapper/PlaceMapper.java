package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.response.PlaceCardResponse;
import com.minor.photo_app.dto.response.PlaceResponse;
import com.minor.photo_app.entity.Photo;
import com.minor.photo_app.entity.Place;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {PhotoMapper.class, TagMapper.class})
public interface PlaceMapper {

    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "photos", source = "photos")
    PlaceResponse toResponse(Place place);

    @Mapping(target = "isFavorite", source = "place", qualifiedByName = "toFavoritePlace")
    @Mapping(target = "mainImageUrl", source = "place", qualifiedByName = "toMainImage")
    PlaceCardResponse toCardResponse(Place place, @Context Set<Long> placeFavoriteIds);

    Set<PlaceCardResponse> toCardResponseSet(Set<Place> places, @Context Set<Long> placeFavoriteIds);

    Set<PlaceResponse> toResponseSet(Set<Place> places);

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
}
