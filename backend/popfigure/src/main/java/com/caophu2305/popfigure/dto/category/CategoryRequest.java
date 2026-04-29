package com.caophu2305.popfigure.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Category name is required")
        @Size(max = 100, message = "Category name must be at most 100 characters")
        String name,

        @NotBlank(message = "Category slug is required")
        @Size(max = 100, message = "Category slug must be at most 100 characters")
        String slug
) {
}

