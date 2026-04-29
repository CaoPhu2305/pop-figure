package com.caophu2305.popfigure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caophu2305.popfigure.entity.BranchInventory;

public interface BranchInventoryRepository extends JpaRepository<BranchInventory, Long> {
    Optional<BranchInventory> findByBranch_IdAndProductVariant_Id(Long branchId, Long productVariantId);
    List<BranchInventory> findByProductVariant_Id(Long productVariantId);
    List<BranchInventory> findByBranch_Id(Long branchId);
}

