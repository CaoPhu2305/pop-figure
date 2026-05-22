package com.caophu2305.popfigure.controller;

import com.caophu2305.popfigure.dto.request.RoleRequest;
import com.caophu2305.popfigure.dto.response.ApiResponse;
import com.caophu2305.popfigure.dto.response.RoleResponse;
import com.caophu2305.popfigure.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * Danh sách roles — yêu cầu ROLE_MANAGE
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    /**
     * Tạo role mới — yêu cầu ROLE_MANAGE
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(request))
                .build();
    }

    /**
     * Gán permissions cho role — yêu cầu ROLE_MANAGE
     * Body: danh sách permissionId, e.g. [1, 2, 3]
     */
    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ApiResponse<RoleResponse> assignPermissions(
            @PathVariable Long id,
            @RequestBody List<Long> permissionIds) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.assignPermissions(id, permissionIds))
                .build();
    }

    /**
     * Xóa role — yêu cầu ROLE_MANAGE
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder().build();
    }
}
