package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.request.CategoryEditRequest;
import com.minor.photo_app.dto.request.CategoryRequest;
import com.minor.photo_app.dto.response.CategoryForFiltersResponse;
import com.minor.photo_app.dto.response.CategoryResponse;
import com.minor.photo_app.dto.response.CategoryShortInfoResponse;
import com.minor.photo_app.entity.Category;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {

    @Mapping(target = "children", source = "children", qualifiedByName = "toResponseSet")
    @Mapping(target = "parentId", source = "parent.id")
    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);

    @Mapping(target = "parentId", source = "parent.id")
    CategoryShortInfoResponse toResponseShort(Category category);
    List<CategoryShortInfoResponse> toResponseShortList(List<Category> categories);

    Category toEntity(CategoryRequest request);
    List<Category> toEntityList(List<CategoryRequest> requests);

    CategoryForFiltersResponse toFiltersResponse(Category category);
    List<CategoryForFiltersResponse> toFiltersResponseList(List<Category> category);

    void updateCategoryFromRequest(CategoryEditRequest request, @MappingTarget Category category);

    @Named("toResponseSet")
    default Set<CategoryResponse> toResponseSet(Set<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return Set.of();
        }

        return categories.stream().map(this::toResponse).collect(Collectors.toSet());
    }
}
