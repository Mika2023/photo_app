package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.response.TagResponse;
import com.minor.photo_app.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResponse toResponse(Tag tag);
}
