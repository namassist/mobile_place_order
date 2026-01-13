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

    public OrderDTO addItem(Long orderId, AddToCartRequest request) {
        Order order;
        // Logic: Jika ID 0 atau null, buat cart baru
        if (orderId == null || orderId == 0) {
            order = new Order();
            order.setStatus(OrderStatus.DRAFT);
            order.setCustomerName("Tom Jerry"); 
            order.setCustomerAddress("Jl. Tali 7 No.9. Jakarta Barat");
            order.setItems(new ArrayList<>());
            order = orderRepository.save(order);
        } else {
            order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));
            
            if (order.getStatus() == OrderStatus.PLACED) {
                throw new OrderOperationException("Cannot modify a placed order.");
            }
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        // Cek apakah item sudah ada di cart
        Optional<OrderItem> existingItem = order.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setSubtotal(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        } else {
            OrderItem newItem = new OrderItem();
            newItem.setOrder(order);
            newItem.setProduct(product);
            newItem.setProductName(product.getName());
            newItem.setProductType(product.getType());
            newItem.setPrice(product.getPrice());
            newItem.setQuantity(request.getQuantity());
            newItem.setSubtotal(product.getPrice().multiply(new BigDecimal(request.getQuantity())));
            order.getItems().add(newItem);
        }

        recalculateOrderTotal(order);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
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
}