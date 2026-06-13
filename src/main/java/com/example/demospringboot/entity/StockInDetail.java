package com.example.demospringboot.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_in_details")
public class StockInDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_in_id", nullable = false)
    private StockIn stockIn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "buy_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal buyPrice;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "expired_date")
    private LocalDate expiredDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public StockInDetail() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StockIn getStockIn() { return stockIn; }
    public void setStockIn(StockIn stockIn) { this.stockIn = stockIn; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getBuyPrice() { return buyPrice; }
    public void setBuyPrice(BigDecimal buyPrice) { this.buyPrice = buyPrice; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public LocalDate getExpiredDate() { return expiredDate; }
    public void setExpiredDate(LocalDate expiredDate) { this.expiredDate = expiredDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
