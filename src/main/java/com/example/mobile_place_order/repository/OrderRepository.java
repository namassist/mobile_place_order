package com.example.mobile_place_order.repository;

import com.example.mobile_place_order.entity.Order;
import com.example.mobile_place_order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByCustomerNameAndStatus(String customerName, OrderStatus status);
}
