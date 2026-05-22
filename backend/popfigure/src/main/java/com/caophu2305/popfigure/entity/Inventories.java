package com.caophu2305.popfigure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table
public class Inventories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @NotBlank
    private Integer stockQuantity;
    
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    
    @ManyToOne
    @JoinColumn(name = "productVariant_id")
    private ProductVariant productVariant;

}
