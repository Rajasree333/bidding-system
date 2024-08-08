package com.javaguides.product.controller;

import com.javaguides.product.dto.ProductRequest;
import com.javaguides.product.dto.ProductResponse;
import com.javaguides.product.entity.Product;
import com.javaguides.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setBasePrice(productRequest.getBasePrice());
        product.setAuctionStartTime(productRequest.getAuctionStartTime());
        product.setAuctionEndTime(productRequest.getAuctionEndTime());

        Product savedProduct = productService.saveProduct(product);
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(savedProduct.getId());
        productResponse.setName(savedProduct.getName());
        productResponse.setCategory(savedProduct.getCategory());
        productResponse.setBasePrice(savedProduct.getBasePrice());
        productResponse.setAuctionStartTime(savedProduct.getAuctionStartTime());
        productResponse.setAuctionEndTime(savedProduct.getAuctionEndTime());

        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponse> productResponses = products.stream().map(product -> {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setCategory(product.getCategory());
            productResponse.setBasePrice(product.getBasePrice());
            productResponse.setAuctionStartTime(product.getAuctionStartTime());
            productResponse.setAuctionEndTime(product.getAuctionEndTime());
            return productResponse;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(productResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setCategory(product.getCategory());
            productResponse.setBasePrice(product.getBasePrice());
            productResponse.setAuctionStartTime(product.getAuctionStartTime());
            productResponse.setAuctionEndTime(product.getAuctionEndTime());
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        List<ProductResponse> productResponses = products.stream().map(product -> {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setCategory(product.getCategory());
            productResponse.setBasePrice(product.getBasePrice());
            productResponse.setAuctionStartTime(product.getAuctionStartTime());
            productResponse.setAuctionEndTime(product.getAuctionEndTime());
            return productResponse;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(productResponses, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
