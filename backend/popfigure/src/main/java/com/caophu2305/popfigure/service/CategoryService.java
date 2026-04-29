package com.caophu2305.popfigure.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caophu2305.popfigure.dto.category.CategoryRequest;
import com.caophu2305.popfigure.dto.category.CategoryResponse;
import com.caophu2305.popfigure.entity.Category;
import com.caophu2305.popfigure.exception.ResourceNotFoundException;
import com.caophu2305.popfigure.repository.CategoryRepository;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    public CategoryResponse create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setSlug(request.slug());
        return toResponse(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findEntity(id);
        category.setName(request.name());
        category.setSlug(request.slug());
        return toResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        Category category = findEntity(id);
        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public Category findEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}

