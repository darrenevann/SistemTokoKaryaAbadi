package com.example.demospringboot.service;

import com.example.demospringboot.entity.*;
import com.example.demospringboot.repository.SaleRepository;
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
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    public Sale processSale(Sale sale, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        // Generate invoice number
        String invoiceNumber = generateInvoiceNumber("TRX");
        sale.setInvoiceNumber(invoiceNumber);
        sale.setUser(user);
        sale.setSoldAt(LocalDateTime.now());

        // Calculate total and reduce stock
        BigDecimal total = BigDecimal.ZERO;
        for (SaleDetail detail : sale.getDetails()) {
            detail.setSale(sale);

            Product product = productService.findById(detail.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan: " + detail.getProduct().getId()));

            // Set prices from current product data
            detail.setSellPrice(product.getSellPrice());
            detail.setBuyPrice(product.getBuyPrice());
            detail.setSubtotal(product.getSellPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
            total = total.add(detail.getSubtotal());

            // Reduce stock
            productService.reduceStock(product, detail.getQuantity());
        }

        // Apply discount
        BigDecimal discountedTotal = total.subtract(sale.getDiscount() != null ? sale.getDiscount() : BigDecimal.ZERO);
        sale.setTotalAmount(discountedTotal);
        sale.setChangeAmount(sale.getPaidAmount().subtract(discountedTotal));
        sale.setStatus("COMPLETED");

        return saleRepository.save(sale);
    }

    public Optional<Sale> findById(Long id) {
        return saleRepository.findById(id);
    }

    public List<Sale> getSalesByDateRange(LocalDateTime start, LocalDateTime end) {
        return saleRepository.findBySoldAtBetweenOrderBySoldAtDesc(start, end);
    }

    public List<Sale> getTodaySales() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);
        return saleRepository.findBySoldAtBetweenOrderBySoldAtDesc(start, end);
    }

    public BigDecimal getTodayRevenue() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);
        return saleRepository.sumTotalAmountBetween(start, end);
    }

    public long getTodaySaleCount() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);
        return saleRepository.countCompletedSalesBetween(start, end);
    }

    private String generateInvoiceNumber(String prefix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + "-" + timestamp;
    }
}
