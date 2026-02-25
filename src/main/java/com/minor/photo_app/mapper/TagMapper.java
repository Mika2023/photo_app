package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.response.TagFullResponse;
import com.minor.photo_app.dto.response.TagResponse;
import com.minor.photo_app.entity.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResponse toResponse(Tag tag);

    TagFullResponse toFullResponse(Tag tag);
    List<TagFullResponse> toFullResponseList(List<Tag> tags);
}
