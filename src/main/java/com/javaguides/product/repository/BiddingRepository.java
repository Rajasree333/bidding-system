package com.javaguides.product.repository;

import com.javaguides.product.entity.Bidding;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BiddingRepository extends JpaRepository<Bidding, Long> {
    List<Bidding> findByProductId(Long productId);
    List<Bidding> findByUserId(Long userId);
}
