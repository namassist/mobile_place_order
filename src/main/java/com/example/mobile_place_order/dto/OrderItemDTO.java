package com.example.mobile_place_order.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private String type;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}