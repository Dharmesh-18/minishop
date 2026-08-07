package com.dharmesh.minishop.product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private Instant created_at;
}
