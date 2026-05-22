package com.caophu2305.popfigure.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String slug;
    private String description;
    private Boolean isVaulted;
    private Long categoryId;
}
