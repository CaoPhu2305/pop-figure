package com.caophu2305.popfigure.dto.supplier;

import java.time.LocalDateTime;

public record SupplierResponse(
        Long id,
        String name,
        String contactInfo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

