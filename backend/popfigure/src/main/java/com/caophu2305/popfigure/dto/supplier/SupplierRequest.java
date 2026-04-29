package com.caophu2305.popfigure.dto.supplier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SupplierRequest(
        @NotBlank(message = "Supplier name is required")
        @Size(max = 100, message = "Supplier name must be at most 100 characters")
        String name,

        String contactInfo
) {
}

