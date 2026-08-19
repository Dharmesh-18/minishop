package com.dharmesh.minishop.order.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class OrderResponseDTO {
    private String orderNumber;
    private String productName;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String status;
    private Instant createdAt;
}