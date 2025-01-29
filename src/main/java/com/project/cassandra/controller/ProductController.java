package com.project.cassandra.controller;


import com.project.cassandra.entity.Product;
import com.project.cassandra.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Product>> getLatestProducts() {
        List<Product> products = productService.getTop3Products();
        return ResponseEntity.ok(products);
    }
}
