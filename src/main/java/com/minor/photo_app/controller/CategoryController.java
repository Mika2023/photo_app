package com.minor.photo_app.controller;

import com.minor.photo_app.dto.request.CategoryEditRequest;
import com.minor.photo_app.dto.request.CategoryRequest;
import com.minor.photo_app.dto.response.CategoryResponse;
import com.minor.photo_app.dto.response.CategoryShortInfoResponse;
import com.minor.photo_app.service.CategoryService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{categoryId}")
    public CategoryShortInfoResponse getCategoryById(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping("/{categoryId}/children")
    public List<CategoryShortInfoResponse> getChildrenByCategoryId(@PathVariable Long categoryId) {
        return categoryService.getChildrenById(categoryId);
    }

    @PostMapping
    public CategoryResponse createCategory(@RequestBody CategoryRequest categoryRequest) {
        return categoryService.createCategory(categoryRequest);
    }

    @PostMapping("/all")
    public List<CategoryShortInfoResponse> createCategories(@RequestBody @NotEmpty List<CategoryRequest> requests) {
        return categoryService.createCategories(requests);
    }

    @PostMapping("/{categoryId}")
    public CategoryResponse updateCategory(@PathVariable Long categoryId, @RequestBody CategoryEditRequest request) {
        return categoryService.editCategory(categoryId, request);
    }

    @PostMapping("/{categoryId}/children")
    public CategoryResponse setChildrenToCategory(@PathVariable Long categoryId,
                                                  @RequestBody @NotEmpty List<CategoryRequest> requests) {
        return categoryService.setChildrenToCategory(requests, categoryId);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
