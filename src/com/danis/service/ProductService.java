package com.danis.service;

import com.danis.dao.ProductDao;
import com.danis.dto.ProductDto;
import com.danis.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductService {
    private ProductDao productDao = ProductDao.getInstance();
    private static ProductService INSTANCE = new ProductService();

    public static ProductService getInstance() {
        return INSTANCE;
    }

    public List<ProductDto> findAll() {
        return productDao.findAll().stream()
                .map(product -> ProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<ProductDto> findByName(String name) {
        return productDao.findByName(name).map(product -> ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build());
    }

    public void save(Product product) {
        productDao.save(product);
    }
}
