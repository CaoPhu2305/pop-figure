package com.caophu2305.popfigure.config;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.caophu2305.popfigure.entity.Role;
import com.caophu2305.popfigure.entity.User;
import com.caophu2305.popfigure.entity.UserStatus;
import com.caophu2305.popfigure.repository.RoleRepository;
import com.caophu2305.popfigure.repository.UserRepository;

@Configuration
public class AuthSeeder {

    @Bean
    CommandLineRunner seedAuthData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Role adminRole = ensureRole(roleRepository, "ADMIN", "System administrator");
            ensureRole(roleRepository, "CUSTOMER", "Default customer role");

            Optional<User> adminUser = userRepository.findByEmail("admin@popfigure.local");
            if (adminUser.isEmpty()) {
                User user = new User();
                user.setName("admin");
                user.setFullName("PopFigure Admin");
                user.setEmail("admin@popfigure.local");
                user.setPassword(passwordEncoder.encode("Admin@123456"));
                user.setPhoneNumber("0900000000");
                user.setRole(adminRole);
                user.setStatus(UserStatus.ACTIVE);
                userRepository.save(user);
            }
        };
    }

    private Role ensureRole(RoleRepository roleRepository, String name, String description) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    role.setDescription(description);
                    return roleRepository.save(role);
                });
    }
}
