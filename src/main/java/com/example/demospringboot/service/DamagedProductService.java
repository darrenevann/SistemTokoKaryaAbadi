package com.example.demospringboot.service;

import com.example.demospringboot.entity.DamagedProduct;
import com.example.demospringboot.entity.Product;
import com.example.demospringboot.entity.User;
import com.example.demospringboot.repository.DamagedProductRepository;
import com.example.demospringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DamagedProductService {

    @Autowired
    private DamagedProductRepository damagedProductRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    public List<DamagedProduct> getAllDamagedProducts() {
        return damagedProductRepository.findAllByOrderByReportedAtDesc();
    }

    public Optional<DamagedProduct> findById(Long id) {
        return damagedProductRepository.findById(id);
    }

    public DamagedProduct reportDamagedProduct(Long productId, Integer quantity,
                                                String reason, String description,
                                                LocalDate reportedAt, String username) {
        Product product = productService.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        // Reduce stock
        productService.reduceStock(product, quantity);

        // Calculate loss amount
        BigDecimal lossAmount = product.getBuyPrice().multiply(BigDecimal.valueOf(quantity));

        DamagedProduct damagedProduct = new DamagedProduct();
        damagedProduct.setProduct(product);
        damagedProduct.setUser(user);
        damagedProduct.setQuantity(quantity);
        damagedProduct.setReason(reason);
        damagedProduct.setDescription(description);
        damagedProduct.setLossAmount(lossAmount);
        damagedProduct.setReportedAt(reportedAt != null ? reportedAt : LocalDate.now());

        return damagedProductRepository.save(damagedProduct);
    }

    public BigDecimal getTotalLossAmount(LocalDate start, LocalDate end) {
        return damagedProductRepository.sumLossAmountBetween(start, end);
    }
}
