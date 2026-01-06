package com.minor.photo_app.service;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.response.CategoryResponse;
import com.minor.photo_app.dto.response.CategoryWithPlacesResponse;
import com.minor.photo_app.entity.Category;
import com.minor.photo_app.exception.NotFoundException;
import com.minor.photo_app.mapper.CategoryMapper;
import com.minor.photo_app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final FavoritePlaceService favoritePlaceService;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findByParentIsNull();
        return categoryMapper.toResponseList(categories);
    }

    @Transactional(readOnly = true)
    public CategoryWithPlacesResponse getCategoryById(Long categoryId, UserPrincipal userPrincipal) {
        Category category = categoryRepository.findByIdWithPlaces(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        Set<Long> favoritePlaces = favoritePlaceService.getFavoritePlacesByUser(userPrincipal);

        return categoryMapper.toResponseWithPlaces(category, favoritePlaces);
    }
}
