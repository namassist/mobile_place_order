package com.example.mobile_place_order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Product Data Transfer Object using Java Record.
 * Immutable by design, with built-in equals(), hashCode(), and toString().
 */
public record ProductDTO(
        Long id,

        @NotBlank(message = "Product name is required")
        @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
        String name,

        @NotBlank(message = "Product type is required")
        @Size(min = 1, max = 100, message = "Product type must be between 1 and 100 characters")
        String type,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price
) {}