package com.dharmesh.minishop.order.mapper;

import com.dharmesh.minishop.order.dto.OrderResponseDTO;
import com.dharmesh.minishop.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderResponseDTO toDTO(Order order);
    Order toEntity(OrderResponseDTO dto);
}