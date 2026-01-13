package com.example.mobile_place_order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String customerName;
    private String address;
    private BigDecimal totalAmount;
    private String status;
    private List<OrderItemDTO> items;
}