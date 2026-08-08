package com.dharmesh.minishop.product.service;

import com.dharmesh.minishop.common.exception.ResourceNotFoundException;
import com.dharmesh.minishop.product.dto.ProductRequestDTO;
import com.dharmesh.minishop.product.dto.ProductResponseDTO;
import com.dharmesh.minishop.product.entity.Product;
import com.dharmesh.minishop.product.mapper.ProductMapper;
import com.dharmesh.minishop.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequestDTO requestDTO;
    private ProductResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Test Laptop")
                .price(BigDecimal.valueOf(50000))
                .stockQuantity(10)
                .build();

        requestDTO = new ProductRequestDTO();
        requestDTO.setName("Test Laptop");
        requestDTO.setPrice(BigDecimal.valueOf(50000));
        requestDTO.setStockQuantity(10);

        responseDTO = new ProductResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Test Laptop");
        responseDTO.setPrice(BigDecimal.valueOf(50000));
        responseDTO.setStockQuantity(10);
    }

    @Test
    @DisplayName(value = "Should created product successfully")
    public void createProduct_Success() {
        when(productMapper.toEntity(any(ProductRequestDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(responseDTO);

        ProductResponseDTO result = productService.createProduct(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(("Test Laptop"));
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName(value = "Should throw ResourceNotFoundException when product not found by id")
    public void getProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No Product found with id: 99");

        verify(productRepository, times(1)).findById(99L);
    }

}
