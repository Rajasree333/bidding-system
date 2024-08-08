package com.javaguides.product.service;

import com.javaguides.product.entity.Product;
import com.javaguides.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getExpiredProducts() {
        return productRepository.findByAuctionEndTimeBefore(LocalDateTime.now());
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

