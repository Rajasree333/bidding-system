package com.javaguides.product.service;

import com.javaguides.product.entity.Product;
import com.javaguides.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class AuctionSchedulerService {

    @Autowired
    private BiddingService biddingService;

    @Autowired
    private ProductRepository productRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void scheduleAuctions() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            if (product.getAuctionEndTime() != null) {
                scheduleAuctionEndTask(product);
            }
        }
    }

    private void scheduleAuctionEndTask(Product product) {
        long delay = Duration.between(LocalDateTime.now(), product.getAuctionEndTime()).toMillis();
        if (delay > 0) {
            scheduler.schedule(() -> {
                biddingService.notifyWinner(product.getId());
                // Optionally update product status here
                // product.setAuctionEndTime(null);
                // productRepository.save(product);
            }, delay, TimeUnit.MILLISECONDS);
        }
    }
}
