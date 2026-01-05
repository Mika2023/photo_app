package com.minor.photo_app.service;

import com.minor.photo_app.dto.response.CategoryResponse;
import com.minor.photo_app.entity.Category;
import com.minor.photo_app.mapper.CategoryMapper;
import com.minor.photo_app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toResponseList(categories);
    }
}
