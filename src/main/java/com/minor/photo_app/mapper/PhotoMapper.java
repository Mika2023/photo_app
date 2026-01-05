package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.response.PlacePhotoResponse;
import com.minor.photo_app.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    @Mapping(target = "placeId", source = "place.id")
    @Mapping(target = "userName", source = "user.nickname")
    PlacePhotoResponse toPlacePhotoResponse(Photo photo);

    Set<PlacePhotoResponse> toSetPlacePhotoResponse(Set<Photo> photos);
}
