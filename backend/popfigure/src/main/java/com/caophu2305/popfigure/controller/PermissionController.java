package com.caophu2305.popfigure.controller;

import com.caophu2305.popfigure.dto.request.PermissionRequest;
import com.caophu2305.popfigure.dto.response.ApiResponse;
import com.caophu2305.popfigure.dto.response.PermissionResponse;
import com.caophu2305.popfigure.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * Xem danh sách quyền — yêu cầu permission ROLE_MANAGE
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    /**
     * Tạo quyền mới — yêu cầu permission ROLE_MANAGE
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(request))
                .build();
    }

    /**
     * Xóa quyền — yêu cầu permission ROLE_MANAGE
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ApiResponse.<Void>builder().build();
    }
}
