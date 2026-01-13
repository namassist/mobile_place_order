package com.example.mobile_place_order.service;

import com.example.mobile_place_order.dto.AddToCartRequest;
import com.example.mobile_place_order.dto.OrderDTO;
import com.example.mobile_place_order.dto.OrderItemDTO;
import com.example.mobile_place_order.entity.Order;
import com.example.mobile_place_order.entity.OrderItem;
import com.example.mobile_place_order.entity.OrderStatus;
import com.example.mobile_place_order.entity.Product;
import com.example.mobile_place_order.repository.OrderRepository;
import com.example.mobile_place_order.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

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
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            
            if (order.getStatus() == OrderStatus.PLACED) {
                throw new RuntimeException("Cannot modify a placed order.");
            }
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

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
        return mapToDTO(savedOrder);
    }

    public OrderDTO placeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getItems().isEmpty()) {
            throw new RuntimeException("Cannot place an empty order.");
        }

        order.setStatus(OrderStatus.PLACED);
        order.setOrderDate(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        return mapToDTO(savedOrder);
    }

    public OrderDTO getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToDTO(order);
    }

    private void recalculateOrderTotal(Order order) {
        BigDecimal total = order.getItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);
    }

    private OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomerName(order.getCustomerName());
        dto.setAddress(order.getCustomerAddress());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());
        
        List<OrderItemDTO> itemDTOs = order.getItems().stream().map(item -> {
            OrderItemDTO itemDto = new OrderItemDTO();
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProductName());
            itemDto.setType(item.getProductType());
            itemDto.setPrice(item.getPrice());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setSubtotal(item.getSubtotal());
            return itemDto;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }
}