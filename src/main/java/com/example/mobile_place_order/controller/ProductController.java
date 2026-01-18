package com.example.mobile_place_order.controller;

import com.example.mobile_place_order.dto.DataResponse;
import com.example.mobile_place_order.dto.PagedResponse;
import com.example.mobile_place_order.dto.ProductDTO;
import com.example.mobile_place_order.dto.UpdateProductRequest;
import com.example.mobile_place_order.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/products")
@Validated
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products", description = "Returns a paginated list of all products sorted by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping
    public PagedResponse<ProductDTO> getAllProducts(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size (1-100)") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return PagedResponse.fromPage(productService.findAll(PageRequest.of(page, size, Sort.by("id"))));
    }

    @Operation(summary = "Get product by ID", description = "Returns a single product by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<ProductDTO>> getProduct(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        return ResponseEntity.ok(DataResponse.of(productService.findById(id)));
    }

    @Operation(summary = "Create a new product", description = "Creates a new product and returns the created resource")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    @PostMapping
    public ResponseEntity<DataResponse<ProductDTO>> createProduct(
            @Parameter(description = "Product data") @Valid @RequestBody ProductDTO productDto) {
        ProductDTO created = productService.create(productDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(DataResponse.of(created));
    }

    @Operation(summary = "Update a product", description = "Partially updates a product - only provided fields are updated")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<DataResponse<ProductDTO>> updateProduct(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Parameter(description = "Fields to update") @Valid @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(DataResponse.of(productService.partialUpdate(id, request)));
    }

    @Operation(summary = "Delete a product", description = "Deletes a product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}