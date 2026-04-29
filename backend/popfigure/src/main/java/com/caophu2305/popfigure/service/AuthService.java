package com.caophu2305.popfigure.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caophu2305.popfigure.dto.auth.AuthResponse;
import com.caophu2305.popfigure.dto.auth.AuthUserResponse;
import com.caophu2305.popfigure.dto.auth.LoginRequest;
import com.caophu2305.popfigure.dto.auth.RegisterRequest;
import com.caophu2305.popfigure.entity.Role;
import com.caophu2305.popfigure.entity.User;
import com.caophu2305.popfigure.entity.UserStatus;
import com.caophu2305.popfigure.exception.ConflictException;
import com.caophu2305.popfigure.repository.RoleRepository;
import com.caophu2305.popfigure.repository.UserRepository;

@Service
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ConflictException("Invalid email or password"));

        return buildResponse(user);
    }

    public AuthResponse register(RegisterRequest request) {
        if (!request.password().equals(request.password_confirmation())) {
            throw new IllegalArgumentException("Password confirmation does not match");
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseGet(() -> roleRepository.save(createRole("CUSTOMER", "Default customer role")));

        User user = new User();
        user.setName(request.name());
        user.setFullName(request.full_name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPhoneNumber(request.phone_number());
        user.setRole(customerRole);
        user.setStatus(UserStatus.ACTIVE);

        return buildResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public AuthUserResponse me(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ConflictException("User not found"));
        return toUserResponse(user);
    }

    private AuthResponse buildResponse(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole() != null ? user.getRole().getName() : "CUSTOMER");
        claims.put("userId", user.getId());

        String token = jwtService.generateToken(user.getEmail(), claims);
        return new AuthResponse(
                toUserResponse(user),
                token,
                "Bearer",
                jwtService.getExpirationMs()
        );
    }

    private AuthUserResponse toUserResponse(User user) {
        return new AuthUserResponse(
                user.getId(),
                user.getName(),
                user.getFullName(),
                user.getEmail(),
                user.getEmailVerifiedAt(),
                user.getPhoneNumber(),
                user.getRole() != null ? user.getRole().getId() : null,
                user.getRole() != null ? user.getRole().getName() : null,
                user.getStatus() != null ? user.getStatus().name().toLowerCase() : null
        );
    }

    private Role createRole(String name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        return role;
    }
}

