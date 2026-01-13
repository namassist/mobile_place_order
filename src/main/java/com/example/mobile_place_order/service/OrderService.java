package com.example.mobile_place_order.service;

import com.example.mobile_place_order.dto.AddToCartRequest;
import com.example.mobile_place_order.dto.OrderDTO;
import com.example.mobile_place_order.entity.Order;
import com.example.mobile_place_order.entity.OrderItem;
import com.example.mobile_place_order.entity.OrderStatus;
import com.example.mobile_place_order.entity.Product;
import com.example.mobile_place_order.exception.OrderNotFoundException;
import com.example.mobile_place_order.exception.OrderOperationException;
import com.example.mobile_place_order.exception.ProductNotFoundException;
import com.example.mobile_place_order.mapper.OrderMapper;
import com.example.mobile_place_order.repository.OrderRepository;
import com.example.mobile_place_order.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderDTO addToCart(AddToCartRequest request) {
        String customerName = "Customer " + request.customerId(); 
        
        Order order = orderRepository.findByCustomerNameAndStatus(customerName, OrderStatus.DRAFT)
                .orElseGet(() -> createNewDraftOrder(customerName));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException(request.productId()));

        Optional<OrderItem> existingItem = order.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.quantity());
            item.setSubtotal(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        } else {
            OrderItem newItem = new OrderItem();
            newItem.setOrder(order);
            newItem.setProduct(product);
            newItem.setProductName(product.getName());
            newItem.setProductType(product.getType());
            newItem.setPrice(product.getPrice());
            newItem.setQuantity(request.quantity());
            newItem.setSubtotal(product.getPrice().multiply(new BigDecimal(request.quantity())));
            order.getItems().add(newItem);
        }

        recalculateOrderTotal(order);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    public OrderDTO getCart(Long customerId) {
        String customerName = "Customer " + customerId;
        Order order = orderRepository.findByCustomerNameAndStatus(customerName, OrderStatus.DRAFT)
                .orElseThrow(() -> new OrderNotFoundException("No active cart found for customer: " + customerId));
        return orderMapper.toDTO(order);
    }

    public OrderDTO checkout(Long customerId) {
        String customerName = "Customer " + customerId;
        Order order = orderRepository.findByCustomerNameAndStatus(customerName, OrderStatus.DRAFT)
                .orElseThrow(() -> new OrderNotFoundException("No active cart found for customer: " + customerId));
        
        if (order.getItems().isEmpty()) {
            throw new OrderOperationException("Cannot checkout an empty cart.");
        }

        order.setStatus(OrderStatus.PLACED);
        order.setOrderDate(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    private Order createNewDraftOrder(String customerName) {
        Order order = new Order();
        order.setStatus(OrderStatus.DRAFT);
        order.setCustomerName(customerName);
        order.setCustomerAddress("Jl. Tali 7 No.9. Jakarta Barat"); // Still hardcoded address for now
        order.setItems(new ArrayList<>());
        return orderRepository.save(order);
    }

    public OrderDTO placeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getItems().isEmpty()) {
            throw new OrderOperationException("Cannot place an empty order.");
        }

        order.setStatus(OrderStatus.PLACED);
        order.setOrderDate(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    public OrderDTO getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderMapper.toDTO(order);
    }

    private void recalculateOrderTotal(Order order) {
        BigDecimal total = order.getItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);
    }

    public OrderDTO removeFromCart(Long customerId, Long productId) {
        String customerName = "Customer " + customerId;
        Order order = orderRepository.findByCustomerNameAndStatus(customerName, OrderStatus.DRAFT)
                .orElseThrow(() -> new OrderNotFoundException("No active cart found for customer: " + customerId));
        
        boolean removed = order.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        if (!removed) {
            throw new OrderOperationException("Product not found in cart: " + productId);
        }
        
        recalculateOrderTotal(order);
        return orderMapper.toDTO(orderRepository.save(order));
    }

    public OrderDTO updateItemQuantity(Long customerId, Long productId, Integer quantity) {
        String customerName = "Customer " + customerId;
        Order order = orderRepository.findByCustomerNameAndStatus(customerName, OrderStatus.DRAFT)
                .orElseThrow(() -> new OrderNotFoundException("No active cart found for customer: " + customerId));
        
        OrderItem item = order.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new OrderOperationException("Product not found in cart: " + productId));
        
        item.setQuantity(quantity);
        item.setSubtotal(item.getPrice().multiply(new BigDecimal(quantity)));
        
        recalculateOrderTotal(order);
        return orderMapper.toDTO(orderRepository.save(order));
    }

    public void clearCart(Long customerId) {
        String customerName = "Customer " + customerId;
        Order order = orderRepository.findByCustomerNameAndStatus(customerName, OrderStatus.DRAFT)
                .orElseThrow(() -> new OrderNotFoundException("No active cart found for customer: " + customerId));
        
        order.getItems().clear();
        order.setTotalAmount(BigDecimal.ZERO);
        orderRepository.save(order);
    }
}