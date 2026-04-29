package com.caophu2305.popfigure.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caophu2305.popfigure.dto.branch.BranchResponse;
import com.caophu2305.popfigure.dto.branch.BranchStockResponse;
import com.caophu2305.popfigure.entity.Branch;
import com.caophu2305.popfigure.entity.BranchInventory;
import com.caophu2305.popfigure.entity.ProductVariant;
import com.caophu2305.popfigure.exception.ResourceNotFoundException;
import com.caophu2305.popfigure.repository.BranchInventoryRepository;
import com.caophu2305.popfigure.repository.BranchRepository;

@Service
@Transactional
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchInventoryRepository branchInventoryRepository;

    public BranchService(BranchRepository branchRepository, BranchInventoryRepository branchInventoryRepository) {
        this.branchRepository = branchRepository;
        this.branchInventoryRepository = branchInventoryRepository;
    }

    @Transactional(readOnly = true)
    public List<BranchResponse> getAll() {
        return branchRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BranchResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional(readOnly = true)
    public List<BranchStockResponse> getStockByVariant(Long variantId) {
        return branchInventoryRepository.findByProductVariant_Id(variantId).stream().map(this::toStockResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<BranchStockResponse> getStockByProduct(Long productId) {
        return branchInventoryRepository.findAll().stream()
                .filter(stock -> stock.getProductVariant() != null
                        && stock.getProductVariant().getProduct() != null
                        && productId.equals(stock.getProductVariant().getProduct().getId()))
                .map(this::toStockResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Branch findEntity(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + id));
    }

    private BranchResponse toResponse(Branch branch) {
        return new BranchResponse(
                branch.getId(),
                branch.getName(),
                branch.getWarehouse() != null ? branch.getWarehouse().getId() : null,
                branch.getWarehouse() != null ? branch.getWarehouse().getName() : null,
                branch.getLocation(),
                branch.getManagerUser() != null ? branch.getManagerUser().getId() : null,
                branch.getManagerUser() != null ? branch.getManagerUser().getFullName() : null,
                branch.getCreatedAt(),
                branch.getUpdatedAt()
        );
    }

    private BranchStockResponse toStockResponse(BranchInventory stock) {
        ProductVariant variant = stock.getProductVariant();
        return new BranchStockResponse(
                stock.getBranch() != null ? stock.getBranch().getId() : null,
                stock.getBranch() != null ? stock.getBranch().getName() : null,
                stock.getBranch() != null ? stock.getBranch().getLocation() : null,
                variant != null ? variant.getId() : null,
                variant != null ? variant.getName() : null,
                stock.getAvailableQuantity()
        );
    }
}

