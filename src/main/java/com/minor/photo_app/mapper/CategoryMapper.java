package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.response.CategoryResponse;
import com.minor.photo_app.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PlaceMapper.class})
public interface CategoryMapper {

    @Mapping(target = "places", source = "places")
    @Mapping(target = "children", source = "children", qualifiedByName = "toResponseSet")
    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);

    @Named("toResponseSet")
    default Set<CategoryResponse> toResponseSet(Set<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return Set.of();
        }

        return categories.stream().map(this::toResponse).collect(Collectors.toSet());
    }
}
