package com.example.mobile_place_order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for adding a product to the shopping cart.
 * Using Java Record for immutability and conciseness.
 */
public record AddToCartRequest(
        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,

        @NotNull(message = "Customer ID is required")
        Long customerId
) {}