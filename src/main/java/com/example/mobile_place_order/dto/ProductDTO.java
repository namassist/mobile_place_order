package com.example.mobile_place_order.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String type;
    private BigDecimal price;
}