package com.example.mobile_place_order.controller;

import com.example.mobile_place_order.dto.AddToCartRequest;
import com.example.mobile_place_order.dto.OrderDTO;
import com.example.mobile_place_order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderCart(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderDTO> addItemToCart(
            @PathVariable Long orderId,
            @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(orderService.addItem(orderId, request));
    }

    @PostMapping("/{orderId}/place")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.placeOrder(orderId));
    }
}