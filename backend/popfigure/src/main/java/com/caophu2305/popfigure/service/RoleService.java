package com.caophu2305.popfigure.service;

import com.caophu2305.popfigure.dto.request.RoleRequest;
import com.caophu2305.popfigure.dto.response.PermissionResponse;
import com.caophu2305.popfigure.dto.response.RoleResponse;
import com.caophu2305.popfigure.entity.Permission;
import com.caophu2305.popfigure.entity.Role;
import com.caophu2305.popfigure.entity.RolePermission;
import com.caophu2305.popfigure.exception.AppException;
import com.caophu2305.popfigure.exception.ErrorCode;
import com.caophu2305.popfigure.repository.PermissionRepository;
import com.caophu2305.popfigure.repository.RolePermissionRepository;
import com.caophu2305.popfigure.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleResponse createRole(RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role = roleRepository.save(role);
        return toResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public RoleResponse assignPermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Remove existing
        rolePermissionRepository.deleteByRoleId(roleId);

        // Add new ones
        for (Long permId : permissionIds) {
            Permission permission = permissionRepository.findById(permId)
                    .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
            RolePermission rp = new RolePermission();
            rp.setRole(role);
            rp.setPermission(permission);
            rolePermissionRepository.save(rp);
        }

        // Reload to get fresh data
        role = roleRepository.findById(roleId).get();
        return toResponse(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    private RoleResponse toResponse(Role role) {
        List<PermissionResponse> permissions = role.getRolePermissions() == null
                ? List.of()
                : role.getRolePermissions().stream()
                        .map(rp -> PermissionResponse.builder()
                                .id(rp.getPermission().getId())
                                .name(rp.getPermission().getName())
                                .description(rp.getPermission().getDescription())
                                .build())
                        .toList();

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(permissions)
                .build();
    }
}
