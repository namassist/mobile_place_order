package com.example.mobile_place_order.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Snapshot data (jaga-jaga kalau data master produk berubah)
    private String productName;
    private String productType;
    private BigDecimal price;

    private Integer quantity;
    private BigDecimal subtotal;
}