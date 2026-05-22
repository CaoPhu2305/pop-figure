package com.caophu2305.popfigure.controller;

import com.caophu2305.popfigure.dto.request.UserCreationRequest;
import com.caophu2305.popfigure.dto.response.ApiResponse;
import com.caophu2305.popfigure.dto.response.UserResponse;
import com.caophu2305.popfigure.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Đăng ký tài khoản — public, không cần login
     */
    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest userCreationRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(userCreationRequest))
                .build();
    }

    /**
     * Xem danh sách users — yêu cầu permission USER_VIEW (chỉ ADMIN)
     */
    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    /**
     * Xem thông tin cá nhân — bất kỳ user đã đăng nhập
     */
    @GetMapping("/myInfo")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }
}
