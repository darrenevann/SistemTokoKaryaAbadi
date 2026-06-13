package com.example.demospringboot.service;

import com.example.demospringboot.entity.PriceHistory;
import com.example.demospringboot.repository.PriceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PriceHistoryService {

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    public List<PriceHistory> getAllPriceHistory() {
        return priceHistoryRepository.findAllByOrderByChangedAtDesc();
    }

    public List<PriceHistory> getPriceHistoryByProduct(Long productId) {
        return priceHistoryRepository.findByProductIdOrderByChangedAtDesc(productId);
    }
}
