package com.caophu2305.popfigure.controller;

import com.caophu2305.popfigure.dto.request.OrderStatusRequest;
import com.caophu2305.popfigure.dto.response.ApiResponse;
import com.caophu2305.popfigure.dto.response.OrderResponse;
import com.caophu2305.popfigure.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Xem đơn hàng của bản thân — bất kỳ user đã đăng nhập
     */
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<OrderResponse>> getMyOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getMyOrders())
                .build();
    }

    /**
     * Xem TẤT CẢ đơn hàng — yêu cầu ORDER_VIEW (ADMIN)
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ORDER_VIEW')")
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getAllOrders())
                .build();
    }

    /**
     * Cập nhật trạng thái đơn hàng — yêu cầu ORDER_MANAGE (ADMIN)
     * Body: { "status": "SHIPPED" }
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ORDER_MANAGE')")
    public ApiResponse<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody OrderStatusRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateStatus(id, request))
                .build();
    }
}
