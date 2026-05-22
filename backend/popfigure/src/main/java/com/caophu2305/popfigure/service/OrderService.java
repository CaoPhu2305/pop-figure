package com.caophu2305.popfigure.service;

import com.caophu2305.popfigure.dto.request.OrderStatusRequest;
import com.caophu2305.popfigure.dto.response.OrderResponse;
import com.caophu2305.popfigure.entity.Order;
import com.caophu2305.popfigure.entity.User;
import com.caophu2305.popfigure.exception.AppException;
import com.caophu2305.popfigure.exception.ErrorCode;
import com.caophu2305.popfigure.repository.OrderRepository;
import com.caophu2305.popfigure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    /**
     * Get all orders of the current authenticated user
     */
    public List<OrderResponse> getMyOrders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return orderRepository.findByUserId(user.getId()).stream().map(this::toResponse).toList();
    }

    /**
     * Get all orders — requires ORDER_VIEW permission (ADMIN only)
     */
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Update order status — requires ORDER_MANAGE permission
     */
    public OrderResponse updateStatus(Long orderId, OrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        order.setStatus(request.getStatus());
        order = orderRepository.save(order);
        return toResponse(order);
    }

    private OrderResponse toResponse(Order o) {
        return OrderResponse.builder()
                .id(o.getId())
                .totalAmount(o.getTotalAmount())
                .status(o.getStatus())
                .createdAt(o.getCreatedAt())
                .userId(o.getUser() != null ? o.getUser().getId() : null)
                .username(o.getUser() != null ? o.getUser().getUsername() : null)
                .build();
    }
}
