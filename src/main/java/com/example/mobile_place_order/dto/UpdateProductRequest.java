package com.example.mobile_place_order.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request DTO for partial product updates (PATCH).
 * All fields are optional - null means "don't update this field".
 */
public record UpdateProductRequest(
        @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
        String name,

        @Size(min = 1, max = 100, message = "Product type must be between 1 and 100 characters")
        String type,

        @Positive(message = "Price must be greater than zero")
        BigDecimal price
) {}
