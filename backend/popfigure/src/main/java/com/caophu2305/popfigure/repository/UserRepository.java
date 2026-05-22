package com.caophu2305.popfigure.repository;

import com.caophu2305.popfigure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    Optional<User> findByUsername(String userName);
    Optional<User> findByEmail(String email);
}
