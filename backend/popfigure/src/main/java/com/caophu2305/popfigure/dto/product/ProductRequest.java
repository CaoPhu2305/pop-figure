package com.caophu2305.popfigure.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(
        @NotNull(message = "Category id is required")
        Long categoryId,

        @NotNull(message = "Supplier id is required")
        Long supplierId,

        @NotBlank(message = "Product name is required")
        @Size(max = 255, message = "Product name must be at most 255 characters")
        String name,

        String description,

        @NotBlank(message = "Product slug is required")
        @Size(max = 255, message = "Product slug must be at most 255 characters")
        String slug,

        @NotBlank(message = "Product status is required")
        String status
) {
}

