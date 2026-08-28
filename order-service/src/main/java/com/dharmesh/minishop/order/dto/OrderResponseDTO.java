package com.dharmesh.minishop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private String orderNumber;
    private Long productId;
    private String username;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}