package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.response.PlaceResponse;
import com.minor.photo_app.entity.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PhotoMapper.class, TagMapper.class})
public interface PlaceMapper {

    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "photos", source = "photos")
    PlaceResponse toResponse(Place place);
}
