package com.example.mobile_place_order.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Order Data Transfer Object using Java Record.
 * Used for returning order/cart information to clients.
 */
public record OrderDTO(
        Long id,
        String customerName,
        String address,
        BigDecimal totalAmount,
        String status,
        List<OrderItemDTO> items
) {}