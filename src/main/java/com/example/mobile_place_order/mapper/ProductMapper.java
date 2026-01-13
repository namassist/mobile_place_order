package com.example.mobile_place_order.mapper;

import com.example.mobile_place_order.dto.ProductDTO;
import com.example.mobile_place_order.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    
    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductDTO productDTO);
}
