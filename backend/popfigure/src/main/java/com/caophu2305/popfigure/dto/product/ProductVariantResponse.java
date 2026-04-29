package com.caophu2305.popfigure.dto.product;

import java.math.BigDecimal;

public record ProductVariantResponse(
        Long id,
        String name,
        String sku,
        BigDecimal price,
        BigDecimal originalPrice,
        String imageUrl
) {
}

