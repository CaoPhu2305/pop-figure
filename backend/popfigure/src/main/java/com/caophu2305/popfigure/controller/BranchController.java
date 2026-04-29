package com.caophu2305.popfigure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caophu2305.popfigure.dto.branch.BranchResponse;
import com.caophu2305.popfigure.dto.branch.BranchStockResponse;
import com.caophu2305.popfigure.service.BranchService;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAll() {
        return ResponseEntity.ok(branchService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.getById(id));
    }

    @GetMapping("/stock/variant/{variantId}")
    public ResponseEntity<List<BranchStockResponse>> getStockByVariant(@PathVariable Long variantId) {
        return ResponseEntity.ok(branchService.getStockByVariant(variantId));
    }

    @GetMapping("/stock/product/{productId}")
    public ResponseEntity<List<BranchStockResponse>> getStockByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(branchService.getStockByProduct(productId));
    }
}

