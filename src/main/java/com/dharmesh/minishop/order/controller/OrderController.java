package com.dharmesh.minishop.order.controller;


import com.dharmesh.minishop.order.dto.OrderRequestDTO;
import com.dharmesh.minishop.order.dto.OrderResponseDTO;
import com.dharmesh.minishop.order.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Endpoints for placing and managing orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO,
                                                       Authentication authentication) {
        OrderResponseDTO response = orderService.placeOrder(orderRequestDTO, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
