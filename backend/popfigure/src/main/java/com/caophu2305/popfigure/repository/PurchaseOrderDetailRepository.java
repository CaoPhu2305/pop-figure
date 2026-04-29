package com.caophu2305.popfigure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caophu2305.popfigure.entity.PurchaseOrderDetail;
import com.caophu2305.popfigure.entity.PurchaseOrderDetailId;

public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, PurchaseOrderDetailId> {
    List<PurchaseOrderDetail> findByPurchaseOrder_Id(Long orderId);
}

