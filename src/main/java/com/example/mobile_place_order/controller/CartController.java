package com.example.mobile_place_order.controller;

import com.example.mobile_place_order.dto.AddToCartRequest;
import com.example.mobile_place_order.dto.OrderDTO;
import com.example.mobile_place_order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> addToCart(@Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(orderService.addToCart(request));
    }

    @GetMapping
    public ResponseEntity<OrderDTO> getCart(@RequestParam Long customerId) {
        return ResponseEntity.ok(orderService.getCart(customerId));
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkout(@RequestParam Long customerId) {
        return ResponseEntity.ok(orderService.checkout(customerId));
    }
}
