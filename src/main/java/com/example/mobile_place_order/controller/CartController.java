package com.example.mobile_place_order.controller;

import com.example.mobile_place_order.dto.AddToCartRequest;
import com.example.mobile_place_order.dto.OrderDTO;
import com.example.mobile_place_order.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Validated
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

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<OrderDTO> removeFromCart(
            @RequestParam Long customerId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(orderService.removeFromCart(customerId, productId));
    }

    @PatchMapping("/items/{productId}")
    public ResponseEntity<OrderDTO> updateItemQuantity(
            @RequestParam Long customerId,
            @PathVariable Long productId,
            @RequestParam @Min(1) Integer quantity) {
        return ResponseEntity.ok(orderService.updateItemQuantity(customerId, productId, quantity));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestParam Long customerId) {
        orderService.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }
}
