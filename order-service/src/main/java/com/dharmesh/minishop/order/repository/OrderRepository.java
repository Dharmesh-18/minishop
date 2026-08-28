package com.dharmesh.minishop.order.repository;

import com.dharmesh.minishop.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
