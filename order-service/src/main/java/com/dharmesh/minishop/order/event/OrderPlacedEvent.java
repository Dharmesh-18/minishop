package com.dharmesh.minishop.order.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent {
    private String orderId;
    private String username;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Instant timestamp;
}