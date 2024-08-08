package com.javaguides.product.repository;

import com.javaguides.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByAuctionEndTimeBefore(LocalDateTime now);
}
