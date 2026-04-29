package com.caophu2305.popfigure.dto.branch;

public record BranchStockResponse(
        Long branchId,
        String branchName,
        String location,
        Long variantId,
        String variantName,
        Integer stock
) {
}

