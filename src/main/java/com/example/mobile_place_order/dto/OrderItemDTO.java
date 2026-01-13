package com.example.mobile_place_order.dto;

import java.math.BigDecimal;

/**
 * Order Item Data Transfer Object using Java Record.
 * Represents a single item in an order/cart.
 */
public record OrderItemDTO(
        Long productId,
        String productName,
        String type,
        BigDecimal price,
        Integer quantity,
        BigDecimal subtotal
) {}