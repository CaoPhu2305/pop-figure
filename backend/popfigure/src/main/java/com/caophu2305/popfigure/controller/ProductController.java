package com.caophu2305.popfigure.controller;

import com.caophu2305.popfigure.dto.request.ProductRequest;
import com.caophu2305.popfigure.dto.response.ApiResponse;
import com.caophu2305.popfigure.dto.response.ProductResponse;
import com.caophu2305.popfigure.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Xem tất cả sản phẩm — public
     */
    @GetMapping
    public ApiResponse<List<ProductResponse>> getAll() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAll())
                .build();
    }

    /**
     * Xem chi tiết sản phẩm — public
     */
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable Long id) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getById(id))
                .build();
    }

    /**
     * Xem sản phẩm theo danh mục — public
     */
    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<ProductResponse>> getByCategory(@PathVariable Long categoryId) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getByCategory(categoryId))
                .build();
    }

    /**
     * Tạo sản phẩm mới — yêu cầu PRODUCT_MANAGE (ADMIN)
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    public ApiResponse<ProductResponse> create(@RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.create(request))
                .build();
    }

    /**
     * Cập nhật sản phẩm — yêu cầu PRODUCT_MANAGE (ADMIN)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    public ApiResponse<ProductResponse> update(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.update(id, request))
                .build();
    }

    /**
     * Xóa sản phẩm — yêu cầu PRODUCT_MANAGE (ADMIN)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.<Void>builder().build();
    }
}
