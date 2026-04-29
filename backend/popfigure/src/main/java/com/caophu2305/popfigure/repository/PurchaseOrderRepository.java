package com.caophu2305.popfigure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caophu2305.popfigure.entity.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByOrderCode(String orderCode);
    List<PurchaseOrder> findByUser_Id(Long userId);
}

