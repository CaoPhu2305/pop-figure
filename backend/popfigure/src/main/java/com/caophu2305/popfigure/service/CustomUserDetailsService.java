package com.caophu2305.popfigure.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.caophu2305.popfigure.entity.User;
import com.caophu2305.popfigure.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + username));

        String roleName = user.getRole() != null ? user.getRole().getName() : "CUSTOMER";
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getStatus() == null || user.getStatus().name().equalsIgnoreCase("ACTIVE"),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()))
        );
    }
}

