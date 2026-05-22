package com.caophu2305.popfigure.repository;

import com.caophu2305.popfigure.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
}
