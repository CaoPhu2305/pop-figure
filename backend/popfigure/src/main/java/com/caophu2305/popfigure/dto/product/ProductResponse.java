package com.caophu2305.popfigure.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        Long categoryId,
        String categoryName,
        Long supplierId,
        String supplierName,
        String name,
        String description,
        String slug,
        String status,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String imageUrl,
        Integer variantCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

