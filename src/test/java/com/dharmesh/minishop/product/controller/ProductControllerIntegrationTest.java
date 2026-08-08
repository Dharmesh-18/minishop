package com.dharmesh.minishop.product.controller;

import com.dharmesh.minishop.product.dto.ProductRequestDTO;
import com.dharmesh.minishop.product.dto.ProductResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class ProductControllerIntegrationTest {

    // Testcontainer Postgres setup
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("minishop_test_db")
            .withUsername("test")
            .withPassword("test");

    // Dynamic Database configuration override
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("End-to-End Test: Create a product and retrieve it by ID")
    public void createAndGetProduct_EndToEnd() throws Exception {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setName("Dockerized Phone");
        requestDTO.setDescription("An amazing phone running in a Docker container.");
        requestDTO.setPrice(BigDecimal.valueOf(100000));
        requestDTO.setStockQuantity(50);

        // 1. Create Product (POST)
        String responseString = mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Dockerized Phone")))
                .andExpect(jsonPath("$.description", is("An amazing phone running in a Docker container.")))
                .andExpect(jsonPath("$.price", is(100000)))
                .andExpect(jsonPath("$.stockQuantity", is(50)))
                .andReturn().getResponse().getContentAsString();

        // 2. Extract Product ID from the response
        Long createdId = objectMapper.readTree(responseString).get("id").asLong();

        mockMvc.perform(get("/api/v1/products/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdId.intValue())))
                .andExpect(jsonPath("$.name", is("Dockerized Phone")));



    }

}
