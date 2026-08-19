package com.dharmesh.minishop.order.service;

import com.dharmesh.minishop.common.exception.BadRequestException;
import com.dharmesh.minishop.common.exception.ResourceNotFoundException;
import com.dharmesh.minishop.order.dto.OrderRequestDTO;
import com.dharmesh.minishop.order.dto.OrderResponseDTO;
import com.dharmesh.minishop.order.entity.Order;
import com.dharmesh.minishop.order.event.OrderPlacedEvent;
import com.dharmesh.minishop.order.producer.OrderEventProducer;
import com.dharmesh.minishop.order.repository.OrderRepository;
import com.dharmesh.minishop.product.entity.Product;
import com.dharmesh.minishop.product.repository.ProductRepository;
import com.dharmesh.minishop.user.entity.User;
import com.dharmesh.minishop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderEventProducer orderEventProducer;

    @Transactional
    @CacheEvict(value = "products", key = "#requestDTO.productId")
    public OrderResponseDTO placeOrder(OrderRequestDTO requestDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + requestDTO.getProductId()));

        if(product.getStockQuantity() < requestDTO.getQuantity()) {
            throw new BadRequestException("Insufficient stock available! Current stock: " + product.getStockQuantity());
        }

        product.setStockQuantity(product.getStockQuantity() - requestDTO.getQuantity());
        productRepository.save(product);

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(requestDTO.getQuantity()));
        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Order order = Order.builder()
                .orderNumber(orderNumber)
                .user(user)
                .product(product)
                .quantity(requestDTO.getQuantity())
                .totalPrice(totalPrice)
                .status("PLACED")
                .createdAt(Instant.now())
                .build();

        orderRepository.save(order);

        OrderPlacedEvent orderPlacedEvent = OrderPlacedEvent.builder()
                .orderId(orderNumber)
                .username(username)
                .productId(product.getId())
                .quantity(requestDTO.getQuantity())
                .totalPrice(totalPrice)
                .timestamp(Instant.now())
                .build();

        orderEventProducer.sendOrderPlaceEvent(orderPlacedEvent);

        return OrderResponseDTO.builder()
                .orderNumber(orderNumber)
                .productName(product.getName())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
