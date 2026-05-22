package com.caophu2305.popfigure.repository;

import com.caophu2305.popfigure.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    void deleteByRoleId(Long roleId);

    long countByRoleId(Long roleId);
}
