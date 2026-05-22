package com.caophu2305.popfigure.config;

import com.caophu2305.popfigure.entity.*;
import com.caophu2305.popfigure.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Seeds all initial data on application startup:
 * - Permissions, Roles, Role-Permissions
 * - Users (admin + regular user) with assigned roles
 * - Categories & Products with sample data
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

        private final PermissionRepository permissionRepository;
        private final RoleRepository roleRepository;
        private final RolePermissionRepository rolePermissionRepository;
        private final UserRepository userRepository;
        private final UserRoleRepository userRoleRepository;
        private final CategoryRepository categoryRepository;
        private final ProductRepository productRepository;
        private final PasswordEncoder passwordEncoder;

        // ─────────────────────────────────────────────
        // Permission definitions
        // ─────────────────────────────────────────────
        private static final Map<String, String> PERMISSIONS = new LinkedHashMap<>();
        static {
                PERMISSIONS.put("USER_VIEW", "Xem danh sách người dùng");
                PERMISSIONS.put("USER_MANAGE", "Tạo/sửa/xoá người dùng");
                PERMISSIONS.put("PRODUCT_VIEW", "Xem sản phẩm");
                PERMISSIONS.put("PRODUCT_MANAGE", "Tạo/sửa/xoá sản phẩm");
                PERMISSIONS.put("CATEGORY_VIEW", "Xem danh mục");
                PERMISSIONS.put("CATEGORY_MANAGE", "Quản lý danh mục");
                PERMISSIONS.put("ORDER_VIEW", "Xem đơn hàng");
                PERMISSIONS.put("ORDER_MANAGE", "Cập nhật trạng thái đơn hàng");
                PERMISSIONS.put("ROLE_MANAGE", "Quản lý vai trò & quyền");
        }

        private static final List<String> ADMIN_PERMISSIONS = List.of(
                        "USER_VIEW", "USER_MANAGE", "PRODUCT_VIEW", "PRODUCT_MANAGE",
                        "CATEGORY_VIEW", "CATEGORY_MANAGE", "ORDER_VIEW", "ORDER_MANAGE", "ROLE_MANAGE");

        private static final List<String> USER_PERMISSIONS = List.of(
                        "PRODUCT_VIEW", "CATEGORY_VIEW", "ORDER_VIEW");

        // ─────────────────────────────────────────────
        // Entry point
        // ─────────────────────────────────────────────
        @Override
        @Transactional
        public void run(String... args) {
                seedPermissions();
                Role adminRole = seedRole("ADMIN", "Quản trị viên toàn quyền", ADMIN_PERMISSIONS);
                Role userRole = seedRole("USER", "Người dùng thông thường", USER_PERMISSIONS);

                seedUser("admin", "Administrator", "admin@popfigure.local", "0900000001", "Admin123!", adminRole);
                seedUser("usertest", "Test User", "user@popfigure.local", "0900000002", "User123!", userRole);

                seedCategoriesAndProducts();

                log.info("✅ DataInitializer: all seed data ready.");
        }

        // ─────────────────────────────────────────────
        // Permissions
        // ─────────────────────────────────────────────
        private void seedPermissions() {
                PERMISSIONS.forEach((name, desc) -> {
                        if (!permissionRepository.existsByName(name)) {
                                Permission p = new Permission();
                                p.setName(name);
                                p.setDescription(desc);
                                permissionRepository.save(p);
                                log.info("  + Permission: {}", name);
                        }
                });
        }

        // ─────────────────────────────────────────────
        // Roles (idempotent)
        // ─────────────────────────────────────────────
        private Role seedRole(String roleName, String roleDesc, List<String> permNames) {
                Role role = roleRepository.findByName(roleName).orElseGet(() -> {
                        Role r = new Role();
                        r.setName(roleName);
                        r.setDescription(roleDesc);
                        Role saved = roleRepository.save(r);
                        log.info("  + Role: {}", roleName);
                        return saved;
                });

                if (rolePermissionRepository.countByRoleId(role.getId()) == 0) {
                        for (String permName : permNames) {
                                permissionRepository.findByName(permName).ifPresent(perm -> {
                                        RolePermission rp = new RolePermission();
                                        rp.setRole(role);
                                        rp.setPermission(perm);
                                        rolePermissionRepository.save(rp);
                                });
                        }
                        log.info("    Assigned {} permissions → {}", permNames.size(), roleName);
                }
                return role;
        }

        // ─────────────────────────────────────────────
        // Users
        // ─────────────────────────────────────────────
        private void seedUser(String username, String name, String email, String phone, String rawPassword, Role role) {
                User user = userRepository.findByUsername(username).orElseGet(User::new);
                user.setUsername(username);
                user.setName(name);
                user.setEmail(email);
                user.setPhone(phone);
                user.setHash_password(passwordEncoder.encode(rawPassword));
                user = userRepository.save(user);

                if (!userRoleRepository.existsByUserIdAndRoleId(user.getId(), role.getId())) {
                        UserRole userRole = new UserRole();
                        userRole.setUser(user);
                        userRole.setRole(role);
                        userRoleRepository.save(userRole);
                }

                log.info("  + User created: {} (role: {})", username, role.getName());
        }

        // ─────────────────────────────────────────────
        // Categories & Products
        // ─────────────────────────────────────────────
        private void seedCategoriesAndProducts() {
                if (categoryRepository.count() > 0)
                        return; // already seeded

                Category figures = createCategory("Scale Figures", "scale-figures");
                Category nendoroid = createCategory("Nendoroid", "nendoroid");
                Category plushies = createCategory("Plushies", "plushies");
                Category limited = createCategory("Limited Edition", "limited-edition");

                // ── Scale Figures ──
                createProduct("Gojo Satoru – Jujutsu Kaisen 1/7",
                                "Mô hình chi tiết cao cấp Gojo Satoru tỉ lệ 1/7, chất liệu PVC cao cấp. " +
                                                "**Chiều cao:** 28 cm | **Nhà sản xuất:** Good Smile Company | **Phát hành:** 2024",
                                "gojo-satoru-1-7", false, figures);

                createProduct("Nezuko Kamado – Demon Slayer 1/8",
                                "Nezuko trong hộp tre, dáng đứng uy nghiêm. Tỉ lệ 1/8. " +
                                                "**Chiều cao:** 22 cm | **Nhà sản xuất:** Aniplex | **Phát hành:** 2024",
                                "nezuko-kamado-1-8", false, figures);

                createProduct("Rem – Re:ZERO Crystal Dress Ver. 1/7",
                                "Re:ZERO Rem phiên bản váy pha lê lộng lẫy. Tỉ lệ 1/7. " +
                                                "**Chiều cao:** 26 cm | **Nhà sản xuất:** Alpha Satellite",
                                "rem-crystal-dress-1-7", false, figures);

                createProduct("Power – Chainsaw Man 1/7 Battle Ver.",
                                "Power trong tư thế chiến đấu, máu tươi và vũ khí đầy uy lực. " +
                                                "**Chiều cao:** 30 cm | **Nhà sản xuất:** Kotobukiya",
                                "power-chainsaw-man-battle-1-7", false, figures);

                // ── Nendoroids ──
                createProduct("Nendoroid Naruto Uzumaki – Sage Mode",
                                "Nendoroid Naruto với phụ kiện chế độ Sage, kerberos và nhiều biểu cảm. " +
                                                "**Nhà sản xuất:** Good Smile Company",
                                "nendoroid-naruto-sage-mode", false, nendoroid);

                createProduct("Nendoroid Saber – Fate/Stay Night",
                                "Artoria Pendragon trong bộ giáp bạc cổ điển. Kèm thanh Excalibur. " +
                                                "**Nhà sản xuất:** Good Smile Company",
                                "nendoroid-saber-fate-stay", false, nendoroid);

                createProduct("Nendoroid Hatsune Miku – 15th Anniversary",
                                "Miku phiên bản đặc biệt kỷ niệm 15 năm. Kèm phụ kiện microphone và tóc lơ lửng. " +
                                                "**Nhà sản xuất:** Good Smile Company",
                                "nendoroid-miku-15th", false, nendoroid);

                // ── Plushies ──
                createProduct("Pikachu Plush XL – Pokémon Center",
                                "Pikachu bông cao cấp kích thước XL 45cm, chất liệu nhung mềm mại. " +
                                                "**Kích thước:** 45 cm | **Nhà sản xuất:** Pokemon Center Official",
                                "pikachu-plush-xl", false, plushies);

                createProduct("Totoro Plush – Studio Ghibli",
                                "Totoro xám lớn chính hãng Studio Ghibli, chất lượng cực kỳ mềm mại. " +
                                                "**Kích thước:** 40 cm | **Nhà sản xuất:** Sekiguchi",
                                "totoro-plush-ghibli", false, plushies);

                // ── Limited ──
                createProduct("Saber Alter – Fate 1/7 Black Dress Limited",
                                "Phiên bản giới hạn Saber Alter váy đen, chỉ 500 chiếc toàn cầu. " +
                                                "**Chiều cao:** 28 cm | **Nhà sản xuất:** Alter | **Số thứ tự:** Limited #001-500",
                                "saber-alter-black-dress-limited", true, limited);

                createProduct("Zero Two – Darling in the FranXX Wedding Ver.",
                                "Zero Two phiên bản áo cưới giới hạn, đặt hàng trước. " +
                                                "**Chiều cao:** 32 cm | **Nhà sản xuất:** Kotobukiya",
                                "zero-two-wedding-limited", true, limited);

                log.info("  + Seeded {} categories & {} products",
                                categoryRepository.count(), productRepository.count());
        }

        private Category createCategory(String name, String slug) {
                Category c = new Category();
                c.setName(name);
                c.setSlug(slug);
                return categoryRepository.save(c);
        }

        private void createProduct(String name, String description, String slug,
                        boolean isVaulted, Category category) {
                Product p = new Product();
                p.setName(name);
                p.setSlug(slug);
                p.setDescription(description);
                p.setIs_vaulted(isVaulted);
                p.setCategory(category);
                productRepository.save(p);
        }
}
