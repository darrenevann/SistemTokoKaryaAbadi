package com.example.demospringboot.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_history")
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "old_sell_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal oldSellPrice;

    @Column(name = "new_sell_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal newSellPrice;

    @Column(name = "old_buy_price", precision = 15, scale = 2)
    private BigDecimal oldBuyPrice;

    @Column(name = "new_buy_price", precision = 15, scale = 2)
    private BigDecimal newBuyPrice;

    @Column(length = 255)
    private String reason;

    @Column(name = "changed_at")
    private LocalDateTime changedAt = LocalDateTime.now();

    public PriceHistory() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BigDecimal getOldSellPrice() { return oldSellPrice; }
    public void setOldSellPrice(BigDecimal oldSellPrice) { this.oldSellPrice = oldSellPrice; }

    public BigDecimal getNewSellPrice() { return newSellPrice; }
    public void setNewSellPrice(BigDecimal newSellPrice) { this.newSellPrice = newSellPrice; }

    public BigDecimal getOldBuyPrice() { return oldBuyPrice; }
    public void setOldBuyPrice(BigDecimal oldBuyPrice) { this.oldBuyPrice = oldBuyPrice; }

    public BigDecimal getNewBuyPrice() { return newBuyPrice; }
    public void setNewBuyPrice(BigDecimal newBuyPrice) { this.newBuyPrice = newBuyPrice; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}
