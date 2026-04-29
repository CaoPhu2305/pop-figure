package com.caophu2305.popfigure.dto.branch;

import java.time.LocalDateTime;

public record BranchResponse(
        Long id,
        String name,
        Long warehouseId,
        String warehouseName,
        String location,
        Long managerUserId,
        String managerName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

