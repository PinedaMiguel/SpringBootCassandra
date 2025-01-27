package com.project.cassandra.repository;

import com.project.cassandra.entity.Product;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends CassandraRepository<Product, String> {
    
    @Query("SELECT * FROM products WHERE bucket = 'recent' ORDER BY created_at DESC LIMIT 3")
    List<Product> findTop3ByOrderByCreatedAtDesc();
}
