package com.example.mobile_place_order.service;

import com.example.mobile_place_order.dto.ProductDTO;
import com.example.mobile_place_order.dto.UpdateProductRequest;
import com.example.mobile_place_order.entity.Product;
import com.example.mobile_place_order.exception.ProductNotFoundException;
import com.example.mobile_place_order.mapper.ProductMapper;
import com.example.mobile_place_order.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDTO);
    }

    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toDTO(product);
    }

    public ProductDTO create(ProductDTO dto) {
        Product product = productMapper.toEntity(dto);
        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    public ProductDTO partialUpdate(Long id, UpdateProductRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        
        // Only update fields that are provided (not null)
        if (request.name() != null) {
            existing.setName(request.name());
        }
        if (request.type() != null) {
            existing.setType(request.type());
        }
        if (request.price() != null) {
            existing.setPrice(request.price());
        }
        
        Product saved = productRepository.save(existing);
        return productMapper.toDTO(saved);
    }
}