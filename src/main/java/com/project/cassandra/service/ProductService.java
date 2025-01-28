package com.project.cassandra.service;

import com.project.cassandra.entity.Product;
import com.project.cassandra.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getTop3Products() {
        return productRepository.findTop3ByOrderByCreatedAtDesc();
    }
}
