package com.caophu2305.popfigure.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.caophu2305.popfigure.entity.Branch;
import com.caophu2305.popfigure.entity.BranchInventory;
import com.caophu2305.popfigure.entity.Category;
import com.caophu2305.popfigure.entity.Product;
import com.caophu2305.popfigure.entity.ProductStatus;
import com.caophu2305.popfigure.entity.ProductVariant;
import com.caophu2305.popfigure.entity.Supplier;
import com.caophu2305.popfigure.entity.Warehouse;
import com.caophu2305.popfigure.repository.BranchInventoryRepository;
import com.caophu2305.popfigure.repository.BranchRepository;
import com.caophu2305.popfigure.repository.CategoryRepository;
import com.caophu2305.popfigure.repository.ProductRepository;
import com.caophu2305.popfigure.repository.ProductVariantRepository;
import com.caophu2305.popfigure.repository.SupplierRepository;
import com.caophu2305.popfigure.repository.WarehouseRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            CategoryRepository categoryRepository,
            SupplierRepository supplierRepository,
            WarehouseRepository warehouseRepository,
            BranchRepository branchRepository,
            ProductRepository productRepository,
            ProductVariantRepository productVariantRepository,
            BranchInventoryRepository branchInventoryRepository
    ) {
        return args -> {
            if (categoryRepository.count() > 0 || supplierRepository.count() > 0 || productRepository.count() > 0) {
                return;
            }

            Category figures = categoryRepository.save(createCategory("Figures", "figures"));
            Category nendoroid = categoryRepository.save(createCategory("Nendoroid", "nendoroids"));
            Category plushies = categoryRepository.save(createCategory("Plushies", "plushies"));
            Category figma = categoryRepository.save(createCategory("Figma", "figma"));

            Supplier estream = supplierRepository.save(createSupplier("eStream", "tokyo@estream.jp"));
            Supplier gsc = supplierRepository.save(createSupplier("Good Smile Company", "support@goodsmile.jp"));
            Supplier kotobukiya = supplierRepository.save(createSupplier("Kotobukiya", "sales@kotobukiya.co.jp"));

            Warehouse tokyoWarehouse = warehouseRepository.save(createWarehouse("Tokyo Warehouse", "Tokyo, Japan"));
            Warehouse hanoiWarehouse = warehouseRepository.save(createWarehouse("Ha Noi Warehouse", "Ha Noi, Vietnam"));

            Branch tokyoBranch = branchRepository.save(createBranch("Tokyo Branch", tokyoWarehouse, "Tokyo, Japan"));
            Branch osakaBranch = branchRepository.save(createBranch("Osaka Branch", tokyoWarehouse, "Osaka, Japan"));
            Branch hanoiBranch = branchRepository.save(createBranch("Ha Noi Branch", hanoiWarehouse, "Ha Noi, Vietnam"));

            Product gojo = productRepository.save(createProduct(figures, gsc, "Gojo Satoru - Limited Ver", "gojo-satoru-limited-ver", ProductStatus.ACTIVE, "Phiên bản giới hạn với hiệu ứng năng lượng xanh nổi bật."));
            Product nezuko = productRepository.save(createProduct(figures, estream, "Nezuko Kamado Figure", "nezuko-kamado-figure", ProductStatus.ACTIVE, "Mẫu figure Nezuko với chất liệu sơn tỉ mỉ và chân đế trong suốt."));
            Product rem = productRepository.save(createProduct(figures, kotobukiya, "Rem Crystal Dress", "rem-crystal-dress", ProductStatus.ACTIVE, "Thiết kế váy pha lê sang trọng với màu xanh đặc trưng của Rem."));
            Product pikachu = productRepository.save(createProduct(plushies, gsc, "Pikachu Plushie XL", "pikachu-plushie-xl", ProductStatus.ACTIVE, "Plushie cỡ lớn với lớp vải mềm và form dáng tròn đáng yêu."));

            ProductVariant gojoStandard = productVariantRepository.save(createVariant(gojo, "Standard", "GOJO-STD", bd(1250000), bd(1490000), "https://images.unsplash.com/photo-1618336753974-aae8e04506aa?q=80&w=1200"));
            ProductVariant gojoDx = productVariantRepository.save(createVariant(gojo, "DX Edition", "GOJO-DX", bd(1680000), bd(1890000), "https://images.unsplash.com/photo-1608889175123-8ee362201f81?q=80&w=1200"));
            ProductVariant nezukoScale = productVariantRepository.save(createVariant(nezuko, "1/8 Scale", "NEZ-18", bd(2450000), bd(2790000), "https://images.unsplash.com/photo-1608889175123-8ee362201f81?q=80&w=1200"));
            ProductVariant nezukoPremium = productVariantRepository.save(createVariant(nezuko, "Premium Ver", "NEZ-PREM", bd(2890000), bd(3190000), "https://images.unsplash.com/photo-1612036782180-6f0b6cd846fe?q=80&w=1200"));
            ProductVariant remCrystal = productVariantRepository.save(createVariant(rem, "Crystal Dress", "REM-CRYSTAL", bd(3890000), bd(4290000), "https://images.unsplash.com/photo-1612036782180-6f0b6cd846fe?q=80&w=1200"));
            ProductVariant pikachuXl = productVariantRepository.save(createVariant(pikachu, "XL Size", "PIKA-XL", bd(890000), bd(990000), "https://images.unsplash.com/photo-1559131397-f94da358f7ca?q=80&w=1200"));

            branchInventoryRepository.saveAll(List.of(
                    createInventory(tokyoBranch, gojoStandard, 12, 0, 5),
                    createInventory(osakaBranch, gojoStandard, 5, 0, 5),
                    createInventory(hanoiBranch, gojoStandard, 3, 0, 5),
                    createInventory(tokyoBranch, nezukoScale, 7, 0, 5),
                    createInventory(osakaBranch, nezukoScale, 0, 0, 5),
                    createInventory(hanoiBranch, nezukoScale, 4, 0, 5),
                    createInventory(tokyoBranch, remCrystal, 6, 0, 5),
                    createInventory(osakaBranch, remCrystal, 2, 0, 5),
                    createInventory(hanoiBranch, remCrystal, 1, 0, 5),
                    createInventory(tokyoBranch, pikachuXl, 9, 0, 5),
                    createInventory(hanoiBranch, pikachuXl, 2, 0, 5)
            ));
        };
    }

    private Category createCategory(String name, String slug) {
        Category category = new Category();
        category.setName(name);
        category.setSlug(slug);
        return category;
    }

    private Supplier createSupplier(String name, String contactInfo) {
        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setContactInfo(contactInfo);
        return supplier;
    }

    private Warehouse createWarehouse(String name, String location) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(name);
        warehouse.setLocation(location);
        return warehouse;
    }

    private Branch createBranch(String name, Warehouse warehouse, String location) {
        Branch branch = new Branch();
        branch.setName(name);
        branch.setWarehouse(warehouse);
        branch.setLocation(location);
        return branch;
    }

    private Product createProduct(Category category, Supplier supplier, String name, String slug, ProductStatus status, String description) {
        Product product = new Product();
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setName(name);
        product.setSlug(slug);
        product.setStatus(status);
        product.setDescription(description);
        return product;
    }

    private ProductVariant createVariant(Product product, String name, String sku, BigDecimal price, BigDecimal originalPrice, String imageUrl) {
        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setName(name);
        variant.setSku(sku);
        variant.setPrice(price);
        variant.setOriginalPrice(originalPrice);
        variant.setImageUrl(imageUrl);
        return variant;
    }

    private BranchInventory createInventory(Branch branch, ProductVariant variant, int onHand, int reserved, int reorderLevel) {
        BranchInventory inventory = new BranchInventory();
        inventory.setBranch(branch);
        inventory.setProductVariant(variant);
        inventory.setQuantityOnHand(onHand);
        inventory.setQuantityReserved(reserved);
        inventory.setReorderLevel(reorderLevel);
        return inventory;
    }

    private BigDecimal bd(long value) {
        return BigDecimal.valueOf(value);
    }
}
