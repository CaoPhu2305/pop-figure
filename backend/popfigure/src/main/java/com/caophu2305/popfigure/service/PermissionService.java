package com.caophu2305.popfigure.service;

import com.caophu2305.popfigure.dto.request.PermissionRequest;
import com.caophu2305.popfigure.dto.response.PermissionResponse;
import com.caophu2305.popfigure.entity.Permission;
import com.caophu2305.popfigure.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionResponse createPermission(PermissionRequest request) {
        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        permission = permissionRepository.save(permission);
        return toResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        return permissionRepository.findAll().stream().map(this::toResponse).toList();
    }

    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

    private PermissionResponse toResponse(Permission p) {
        return PermissionResponse.builder()
                .name(p.getName())
                .description(p.getDescription())
                .build();
    }
}
