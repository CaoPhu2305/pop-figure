package com.caophu2305.popfigure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caophu2305.popfigure.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPurchaseOrder_Id(Long orderId);
}
