package com.caophu2305.popfigure.service;

import com.caophu2305.popfigure.dto.request.CategoryRequest;
import com.caophu2305.popfigure.dto.response.CategoryResponse;
import com.caophu2305.popfigure.entity.Category;
import com.caophu2305.popfigure.exception.AppException;
import com.caophu2305.popfigure.exception.ErrorCode;
import com.caophu2305.popfigure.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    public CategoryResponse getById(Long id) {
        return categoryRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public CategoryResponse create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category = categoryRepository.save(category);
        return toResponse(category);
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category = categoryRepository.save(category);
        return toResponse(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryResponse toResponse(Category c) {
        return CategoryResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .slug(c.getSlug())
                .build();
    }
}
