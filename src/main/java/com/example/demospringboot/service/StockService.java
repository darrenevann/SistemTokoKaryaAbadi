package com.example.demospringboot.service;

import com.example.demospringboot.entity.*;
import com.example.demospringboot.repository.StockInRepository;
import com.example.demospringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StockService {

    @Autowired
    private StockInRepository stockInRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    public List<StockIn> getAllStockIn() {
        return stockInRepository.findAllOrderByCreatedAtDesc();
    }

    public Optional<StockIn> findById(Long id) {
        return stockInRepository.findById(id);
    }

    public StockIn createStockIn(StockIn stockIn, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        // Generate invoice number
        String invoiceNumber = generateInvoiceNumber("STK");
        stockIn.setInvoiceNumber(invoiceNumber);
        stockIn.setUser(user);

        // Calculate total and update stock
        BigDecimal total = BigDecimal.ZERO;
        for (StockInDetail detail : stockIn.getDetails()) {
            detail.setStockIn(stockIn);
            detail.setSubtotal(detail.getBuyPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
            total = total.add(detail.getSubtotal());

            // Add stock to product
            Product product = productService.findById(detail.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));
            productService.addStock(product, detail.getQuantity());
        }

        stockIn.setTotalAmount(total);
        return stockInRepository.save(stockIn);
    }

    private String generateInvoiceNumber(String prefix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + "-" + timestamp;
    }

    public List<StockIn> getStockInByDateRange(LocalDate start, LocalDate end) {
        return stockInRepository.findByReceivedAtBetweenOrderByCreatedAtDesc(start, end);
    }
}
