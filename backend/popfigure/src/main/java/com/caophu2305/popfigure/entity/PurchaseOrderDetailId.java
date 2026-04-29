package com.caophu2305.popfigure.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class PurchaseOrderDetailId implements Serializable {

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "product_variant_id")
    private Long productVariantId;
}

