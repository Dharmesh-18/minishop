package com.dharmesh.minishop.order.service;

import com.dharmesh.minishop.common.exception.ResourceNotFoundException;
import com.dharmesh.minishop.order.client.ProductClient;
import com.dharmesh.minishop.order.dto.OrderRequestDTO;
import com.dharmesh.minishop.order.dto.OrderResponseDTO;
import com.dharmesh.minishop.order.entity.Order;
import com.dharmesh.minishop.order.event.OrderPlacedEvent;
import com.dharmesh.minishop.order.mapper.OrderMapper;
import com.dharmesh.minishop.order.producer.OrderEventProducer;
import com.dharmesh.minishop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient; // 👈 Eureka OpenFeign Call
    private final OrderEventProducer orderEventProducer;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO, String username) {
        log.info("Fetching product details from PRODUCT-SERVICE for productId: {}", requestDTO.getProductId());

        // 1. Fetch product via Feign Client from product-service
        ProductClient.ProductResponse product = productClient.getProductById(requestDTO.getProductId());
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id: " + requestDTO.getProductId());
        }

        // 2. Validate Stock
        if (product.stockQuantity() < requestDTO.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + product.stockQuantity());
        }

        // 3. Calculate Total Price
        BigDecimal totalPrice = product.price().multiply(BigDecimal.valueOf(requestDTO.getQuantity()));

        // 4. Save Order
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .productId(product.id())
                .username(username != null ? username : "anonymous")
                .quantity(requestDTO.getQuantity())
                .totalPrice(totalPrice)
                .orderStatus("PLACED")
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", savedOrder.getId());

        // 5. Publish Kafka Event
        // 5. Publish Kafka Event using Lombok Builder
        OrderPlacedEvent orderPlacedEvent = OrderPlacedEvent.builder()
                .orderId(savedOrder.getOrderNumber())
                .username(savedOrder.getUsername())
                .productId(savedOrder.getProductId())
                .quantity(savedOrder.getQuantity())
                .totalPrice(savedOrder.getTotalPrice())
                .timestamp(Instant.now())
                .build();

        orderEventProducer.sendOrderPlaceEvent(orderPlacedEvent);
        orderEventProducer.sendOrderPlaceEvent(orderPlacedEvent);
        return orderMapper.toDTO(savedOrder);
    }

    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toDTO(order);
    }
}