package com.dharmesh.minishop.product.consumer;

import com.dharmesh.minishop.product.event.OrderPlacedEvent;
import com.dharmesh.minishop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductInventoryConsumer {

    private final ProductService productService;

    @KafkaListener(topics = "order-placed-topic", groupId = "product-inventory-group")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Received OrderPlacedEvent for order: {}, deducting stock for productId: {}, quantity: {}",
                event.getOrderId(), event.getProductId(), event.getQuantity());

        try {
            productService.reduceStock(event.getProductId(), event.getQuantity());
        } catch (Exception ex) {
            log.error("Failed to reduce stock for order: {}. Reason: {}", event.getOrderId(), ex.getMessage());
        }
    }
}