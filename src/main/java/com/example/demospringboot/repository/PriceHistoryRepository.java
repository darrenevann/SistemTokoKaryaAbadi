package com.example.demospringboot.repository;

import com.example.demospringboot.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    List<PriceHistory> findByProductIdOrderByChangedAtDesc(Long productId);
    List<PriceHistory> findAllByOrderByChangedAtDesc();
}
