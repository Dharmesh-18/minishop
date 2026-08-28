package com.dharmesh.minishop.order.controller;

import com.dharmesh.minishop.order.dto.OrderRequestDTO;
import com.dharmesh.minishop.order.dto.OrderResponseDTO;
import com.dharmesh.minishop.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Endpoints for managing orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create new order")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO requestDTO,
            @RequestHeader(value = "X-User-Name", defaultValue = "guest_user") String username) {
        OrderResponseDTO responseDTO = orderService.createOrder(requestDTO, username);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        OrderResponseDTO responseDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(responseDTO);
    }
}