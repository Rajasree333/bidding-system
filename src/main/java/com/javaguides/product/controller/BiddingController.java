package com.javaguides.product.controller;

import com.javaguides.product.dto.BiddingRequest;
import com.javaguides.product.dto.BiddingResponse;
import com.javaguides.product.entity.Bidding;
import com.javaguides.product.entity.Product;
import com.javaguides.product.entity.User;
import com.javaguides.product.service.BiddingService;
import com.javaguides.product.service.ProductService;
import com.javaguides.product.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bids")
public class BiddingController {

    @Autowired
    private BiddingService biddingService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<BiddingResponse> placeBid(@RequestBody BiddingRequest biddingRequest) {
        // Fetch Product and User entities
        Product product = productService.getProductById(biddingRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User user = userService.getUserById(biddingRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create and set bid
        Bidding bidding = new Bidding();
        bidding.setProduct(product);
        bidding.setUser(user);
        bidding.setBidAmount(biddingRequest.getBidAmount());
        bidding.setBidTime(biddingRequest.getBidTime());

        // Save bid
        Bidding savedBidding = biddingService.placeBid(bidding);

        // Create response
        BiddingResponse biddingResponse = new BiddingResponse();
        biddingResponse.setId(savedBidding.getId());
        biddingResponse.setProductId(savedBidding.getProduct().getId());
        biddingResponse.setUserId(savedBidding.getUser().getId());
        biddingResponse.setBidAmount(savedBidding.getBidAmount());
        biddingResponse.setBidTime(savedBidding.getBidTime());

        return new ResponseEntity<>(biddingResponse, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<BiddingResponse>> getBidsForProduct(@PathVariable Long productId) {
        List<Bidding> bids = biddingService.getBidsForProduct(productId);
        List<BiddingResponse> biddingResponses = bids.stream().map(bid -> {
            BiddingResponse biddingResponse = new BiddingResponse();
            biddingResponse.setId(bid.getId());
            biddingResponse.setProductId(bid.getProduct().getId());
            biddingResponse.setUserId(bid.getUser().getId());
            biddingResponse.setBidAmount(bid.getBidAmount());
            biddingResponse.setBidTime(bid.getBidTime());
            return biddingResponse;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(biddingResponses, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BiddingResponse>> getBidsForUser(@PathVariable Long userId) {
        List<Bidding> bids = biddingService.getBidsForUser(userId);
        List<BiddingResponse> biddingResponses = bids.stream().map(bid -> {
            BiddingResponse biddingResponse = new BiddingResponse();
            biddingResponse.setId(bid.getId());
            biddingResponse.setProductId(bid.getProduct().getId());
            biddingResponse.setUserId(bid.getUser().getId());
            biddingResponse.setBidAmount(bid.getBidAmount());
            biddingResponse.setBidTime(bid.getBidTime());
            return biddingResponse;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(biddingResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BiddingResponse> getBidById(@PathVariable Long id) {
        Optional<Bidding> bidOptional = biddingService.getBidById(id);
        if (bidOptional.isPresent()) {
            Bidding bid = bidOptional.get();
            BiddingResponse biddingResponse = new BiddingResponse();
            biddingResponse.setId(bid.getId());
            biddingResponse.setProductId(bid.getProduct().getId());
            biddingResponse.setUserId(bid.getUser().getId());
            biddingResponse.setBidAmount(bid.getBidAmount());
            biddingResponse.setBidTime(bid.getBidTime());
            return new ResponseEntity<>(biddingResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBid(@PathVariable Long id) {
        biddingService.deleteBid(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}