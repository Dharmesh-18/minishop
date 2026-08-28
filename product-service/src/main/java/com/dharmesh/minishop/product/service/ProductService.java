package com.dharmesh.minishop.product.service;

import com.dharmesh.minishop.common.exception.ResourceNotFoundException;
import com.dharmesh.minishop.common.dto.PagedResponseDTO;
import com.dharmesh.minishop.product.dto.ProductRequestDTO;
import com.dharmesh.minishop.product.dto.ProductResponseDTO;
import com.dharmesh.minishop.product.entity.Product;
import com.dharmesh.minishop.product.mapper.ProductMapper;
import com.dharmesh.minishop.product.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        Product product = productMapper.toEntity(requestDTO);
        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }

    @Cacheable(value = "products", key = "#id")
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Product found with id: " + id));

        return productMapper.toDto(product);
    }

    public PagedResponseDTO<ProductResponseDTO> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponseDTO> content = productPage.getContent().stream()
                .map(productMapper::toDto)
                .toList();

        return PagedResponseDTO.<ProductResponseDTO>builder()
                .content(content)
                .pageNo(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Product found with id: " + id));
        productRepository.delete(product);
    }


}
