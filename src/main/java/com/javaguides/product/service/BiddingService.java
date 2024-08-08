package com.javaguides.product.service;

import com.javaguides.product.entity.Bidding;
import com.javaguides.product.entity.User;
import com.javaguides.product.repository.BiddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BiddingService {

    @Autowired
    private BiddingRepository biddingRepository;
    @Autowired
    private NotificationService notificationService;

    public Bidding placeBid(Bidding bidding) {
        // Add any business logic here if needed
        return biddingRepository.save(bidding);
    }

    public List<Bidding> getBidsForProduct(Long productId) {
        return biddingRepository.findByProductId(productId);
    }

    public List<Bidding> getBidsForUser(Long userId) {
        return biddingRepository.findByUserId(userId);
    }

    public Optional<Bidding> getBidById(Long id) {
        return biddingRepository.findById(id);
    }

    public void deleteBid(Long id) {
        biddingRepository.deleteById(id);
    }
    public void notifyWinner(Long productId) {
        List<Bidding> bids = biddingRepository.findByProductId(productId);
        if (bids.isEmpty()) {
            return;
        }

        // Find the highest bid
        Bidding highestBid = bids.stream()
                .max((b1, b2) -> b1.getBidAmount().compareTo(b2.getBidAmount()))
                .orElseThrow(() -> new RuntimeException("No bids found for product"));

        User winner = highestBid.getUser();
        sendNotification(winner, productId, highestBid.getBidAmount());
    }
    private void sendNotification(User user, Long productId, BigDecimal bidAmount) {
        String subject = "Congratulations! You've won the bid";
        String text = String.format("Dear %s,\n\nCongratulations! You've won the bid for product ID %d with an amount of %s.\n\nThank you for participating!",
                user.getUsername(), productId, bidAmount.toString());

        notificationService.sendNotification(user.getEmail(), subject, text);
    }
}
