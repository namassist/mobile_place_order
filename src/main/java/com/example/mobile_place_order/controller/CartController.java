package com.example.mobile_place_order.controller;

import com.example.mobile_place_order.dto.AddToCartRequest;
import com.example.mobile_place_order.dto.OrderDTO;
import com.example.mobile_place_order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Shopping Cart", description = "Cart management APIs for adding, viewing, and managing cart items")
public class CartController {

    private final OrderService orderService;

    @Operation(summary = "Add product to cart", description = "Adds a product to the customer's cart. Creates a new cart if none exists.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping
    public ResponseEntity<OrderDTO> addToCart(
            @Parameter(description = "Add to cart request") @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(orderService.addToCart(request));
    }

    @Operation(summary = "Get cart contents", description = "Returns the current cart contents for a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No active cart found")
    })
    @GetMapping
    public ResponseEntity<OrderDTO> getCart(
            @Parameter(description = "Customer ID") @RequestParam Long customerId) {
        return ResponseEntity.ok(orderService.getCart(customerId));
    }

    @Operation(summary = "Checkout cart", description = "Places the order and changes cart status from DRAFT to PLACED")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot checkout empty cart"),
            @ApiResponse(responseCode = "404", description = "No active cart found")
    })
    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkout(
            @Parameter(description = "Customer ID") @RequestParam Long customerId) {
        return ResponseEntity.ok(orderService.checkout(customerId));
    }

    @Operation(summary = "Remove item from cart", description = "Removes a specific product from the cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item removed successfully"),
            @ApiResponse(responseCode = "400", description = "Product not in cart"),
            @ApiResponse(responseCode = "404", description = "No active cart found")
    })
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<OrderDTO> removeFromCart(
            @Parameter(description = "Customer ID") @RequestParam Long customerId,
            @Parameter(description = "Product ID to remove") @PathVariable Long productId) {
        return ResponseEntity.ok(orderService.removeFromCart(customerId, productId));
    }

    @Operation(summary = "Update item quantity", description = "Updates the quantity of a product in the cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quantity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid quantity or product not in cart"),
            @ApiResponse(responseCode = "404", description = "No active cart found")
    })
    @PatchMapping("/items/{productId}")
    public ResponseEntity<OrderDTO> updateItemQuantity(
            @Parameter(description = "Customer ID") @RequestParam Long customerId,
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @Parameter(description = "New quantity (minimum 1)") @RequestParam @Min(1) Integer quantity) {
        return ResponseEntity.ok(orderService.updateItemQuantity(customerId, productId, quantity));
    }

    @Operation(summary = "Clear cart", description = "Removes all items from the cart")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "404", description = "No active cart found")
    })
    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @Parameter(description = "Customer ID") @RequestParam Long customerId) {
        orderService.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }
}
