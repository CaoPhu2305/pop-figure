package com.caophu2305.popfigure.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caophu2305.popfigure.dto.product.ProductDetailResponse;
import com.caophu2305.popfigure.dto.product.ProductRequest;
import com.caophu2305.popfigure.dto.product.ProductResponse;
import com.caophu2305.popfigure.dto.product.ProductVariantResponse;
import com.caophu2305.popfigure.entity.Category;
import com.caophu2305.popfigure.entity.Product;
import com.caophu2305.popfigure.entity.ProductStatus;
import com.caophu2305.popfigure.entity.ProductVariant;
import com.caophu2305.popfigure.entity.Supplier;
import com.caophu2305.popfigure.exception.ResourceNotFoundException;
import com.caophu2305.popfigure.repository.CategoryRepository;
import com.caophu2305.popfigure.repository.ProductRepository;
import com.caophu2305.popfigure.repository.ProductVariantRepository;
import com.caophu2305.popfigure.repository.SupplierRepository;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductVariantRepository productVariantRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SupplierRepository supplierRepository,
            ProductVariantRepository productVariantRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> search(String keyword, Long categoryId, int page, int size) {
        int pageIndex = Math.max(page, 0);
        int pageSize = Math.max(size, 1);

        List<Product> items = new ArrayList<>(productRepository.findAll());
        if (keyword != null && !keyword.isBlank()) {
            String normalized = keyword.trim().toLowerCase();
            items = items.stream()
                    .filter(product -> containsIgnoreCase(product.getName(), normalized)
                            || containsIgnoreCase(product.getDescription(), normalized)
                            || containsIgnoreCase(product.getSlug(), normalized))
                    .toList();
        }
        if (categoryId != null) {
            items = items.stream()
                    .filter(product -> product.getCategory() != null && categoryId.equals(product.getCategory().getId()))
                    .toList();
        }

        int fromIndex = Math.min(pageIndex * pageSize, items.size());
        int toIndex = Math.min(fromIndex + pageSize, items.size());
        List<Product> pagedItems = items.subList(fromIndex, toIndex);

        return new org.springframework.data.domain.PageImpl<>(
                pagedItems.stream().map(this::toResponse).toList(),
                org.springframework.data.domain.PageRequest.of(pageIndex, pageSize),
                items.size()
        );
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getById(Long id) {
        return toDetailResponse(findEntity(id));
    }

    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        applyRequest(product, request);
        return toResponse(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findEntity(id);
        applyRequest(product, request);
        return toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        Product product = findEntity(id);
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public Product findEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    private void applyRequest(Product product, ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + request.categoryId()));
        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + request.supplierId()));

        product.setCategory(category);
        product.setSupplier(supplier);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setSlug(request.slug());
        product.setStatus(parseStatus(request.status()));
    }

    private ProductStatus parseStatus(String status) {
        try {
            return ProductStatus.valueOf(status.trim().toUpperCase());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid product status: " + status);
        }
    }

    private ProductResponse toResponse(Product product) {
        List<ProductVariant> variants = safeVariants(product);
        BigDecimal minPrice = variants.stream()
                .map(ProductVariant::getPrice)
                .min(Comparator.naturalOrder())
                .orElse(null);
        BigDecimal maxPrice = variants.stream()
                .map(ProductVariant::getPrice)
                .max(Comparator.naturalOrder())
                .orElse(null);
        String imageUrl = variants.stream()
                .map(ProductVariant::getImageUrl)
                .filter(value -> value != null && !value.isBlank())
                .findFirst()
                .orElse(null);

        return new ProductResponse(
                product.getId(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getSupplier() != null ? product.getSupplier().getId() : null,
                product.getSupplier() != null ? product.getSupplier().getName() : null,
                product.getName(),
                product.getDescription(),
                product.getSlug(),
                product.getStatus() != null ? product.getStatus().name().toLowerCase() : null,
                minPrice,
                maxPrice,
                imageUrl,
                variants.size(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    private ProductDetailResponse toDetailResponse(Product product) {
        List<ProductVariant> variants = safeVariants(product);
        BigDecimal minPrice = variants.stream()
                .map(ProductVariant::getPrice)
                .min(Comparator.naturalOrder())
                .orElse(null);
        BigDecimal maxPrice = variants.stream()
                .map(ProductVariant::getPrice)
                .max(Comparator.naturalOrder())
                .orElse(null);
        String imageUrl = variants.stream()
                .map(ProductVariant::getImageUrl)
                .filter(value -> value != null && !value.isBlank())
                .findFirst()
                .orElse(null);

        List<ProductVariantResponse> variantResponses = variants.stream()
                .map(variant -> new ProductVariantResponse(
                        variant.getId(),
                        variant.getName(),
                        variant.getSku(),
                        variant.getPrice(),
                        variant.getOriginalPrice(),
                        variant.getImageUrl()
                ))
                .toList();

        return new ProductDetailResponse(
                product.getId(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getSupplier() != null ? product.getSupplier().getId() : null,
                product.getSupplier() != null ? product.getSupplier().getName() : null,
                product.getName(),
                product.getDescription(),
                product.getSlug(),
                product.getStatus() != null ? product.getStatus().name().toLowerCase() : null,
                minPrice,
                maxPrice,
                imageUrl,
                variantResponses.size(),
                variantResponses,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    private List<ProductVariant> safeVariants(Product product) {
        List<ProductVariant> variants = product.getVariants();
        return variants == null ? List.of() : variants;
    }

    private boolean containsIgnoreCase(String value, String normalizedKeyword) {
        return value != null && value.toLowerCase().contains(normalizedKeyword);
    }
}
