package com.dharmesh.minishop.product.mapper;

import com.dharmesh.minishop.product.dto.ProductRequestDTO;
import com.dharmesh.minishop.product.dto.ProductResponseDTO;
import com.dharmesh.minishop.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequestDTO dto);

    ProductResponseDTO toDto(Product entity);

    void updateEntityFromDto(ProductRequestDTO dto, @MappingTarget Product entity);
}
