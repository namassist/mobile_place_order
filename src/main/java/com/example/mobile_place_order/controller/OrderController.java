package com.example.mobile_place_order.controller;

import com.example.mobile_place_order.dto.AddToCartRequest;
import com.example.mobile_place_order.dto.OrderDTO;
import com.example.mobile_place_order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderCart(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderDTO> addItemToCart(
            @PathVariable Long orderId,
            @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(orderService.addItem(orderId, request));
    }

    @PostMapping("/{orderId}/place")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.placeOrder(orderId));
    }
}