package com.example.mobile_place_order.mapper;

import com.example.mobile_place_order.dto.OrderDTO;
import com.example.mobile_place_order.dto.OrderItemDTO;
import com.example.mobile_place_order.entity.Order;
import com.example.mobile_place_order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    
    @Mapping(target = "address", source = "customerAddress")
    OrderDTO toDTO(Order order);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "type", source = "productType")
    OrderItemDTO toDTO(OrderItem orderItem);
}
