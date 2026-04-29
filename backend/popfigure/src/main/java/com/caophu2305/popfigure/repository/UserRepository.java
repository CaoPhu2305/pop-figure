package com.caophu2305.popfigure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caophu2305.popfigure.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

