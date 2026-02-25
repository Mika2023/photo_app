package com.minor.photo_app.service;

import com.minor.photo_app.dto.request.CategoryEditRequest;
import com.minor.photo_app.dto.request.CategoryRequest;
import com.minor.photo_app.dto.response.CategoryForFiltersResponse;
import com.minor.photo_app.dto.response.CategoryResponse;
import com.minor.photo_app.dto.response.CategoryShortInfoResponse;
import com.minor.photo_app.entity.Category;
import com.minor.photo_app.exception.NotFoundException;
import com.minor.photo_app.mapper.CategoryMapper;
import com.minor.photo_app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findByParentIsNull();
        return categoryMapper.toResponseList(categories);
    }

    public List<CategoryForFiltersResponse> getCategoriesForFilters() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toFiltersResponseList(categories);
    }

    public CategoryShortInfoResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с таким id не найдена - " + categoryId));

        return categoryMapper.toResponseShort(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryShortInfoResponse> getChildrenById(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Категория с таким id не найдена - " + categoryId);
        }

        List<Category> children = categoryRepository.findAllByParentId(categoryId);
        return categoryMapper.toResponseShortList(children);
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = prepareCategoryToInsert(request);

        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    private Category prepareCategoryToInsert(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Родительской категории с таким id = %d не существует",
                                    request.getParentId())
                    ));

            category.setParent(parent);
        }
        return category;
    }

    @Transactional
    public List<CategoryShortInfoResponse> createCategories(List<CategoryRequest> requests) {
        requests = requests.stream()
                .filter(r -> Boolean.FALSE.equals(categoryRepository.existsByName(r.getName()))).toList();

        List<Category> categories = new ArrayList<>();
        requests.forEach(r -> categories.add(prepareCategoryToInsert(r)));
        List<Category> saved = categoryRepository.saveAll(categories);
        return categoryMapper.toResponseShortList(saved);
    }

    @Transactional
    public CategoryResponse setChildrenToCategory(List<CategoryRequest> children, Long parentId) {
        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Родительской категории с таким id = %d не существует", parentId)
                ));

        List<Category> categories = categoryMapper.toEntityList(children);
        categories.forEach(parent::addChild);

        Category saved = categoryRepository.save(parent);
        return categoryMapper.toResponse(saved);
    }

    @Transactional
    public CategoryResponse editCategory(Long categoryId, CategoryEditRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Категории с таким id = %d не существует", categoryId)
                ));
        categoryMapper.updateCategoryFromRequest(request, category);
        return categoryMapper.toResponse(category);
    }

    @Transactional(readOnly = true)
    public Set<Category> getExistCategoriesByIds(List<Long> categoryIds) {
        Set<Category> result = new HashSet<>();

        for (Long categoryId: categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Категории с таким id = %d не существует", categoryId)
                    ));

            extendCategoryListByParents(category, result);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public Category getCategoryFromId(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Категории с таким id = %d не существует", categoryId)
                ));
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Категории с таким id = %d не существует",
                        categoryId)));
        categoryRepository.delete(category);
    }

    private void extendCategoryListByParents(Category category, Set<Category> result) {
        Category current = category;

        while (current != null && result.add(current)) {
            current = current.getParent();
        }
    }
}
