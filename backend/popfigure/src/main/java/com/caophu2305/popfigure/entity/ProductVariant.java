package com.caophu2305.popfigure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String sku;

    @NotBlank
    private String name;

    @NotBlank
    private Integer boxNumber;

    @NotBlank
    private BigDecimal price;

    @NotBlank
    private BigDecimal salePrice;

    @NotBlank
    private String imageURL;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productVariant")
    private List<Inventories> inventories;

    @OneToMany(mappedBy = "productVariant")
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "productVariant")
    private List<OrderDetail> orderDetails;

}
