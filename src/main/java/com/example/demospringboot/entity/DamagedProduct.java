package com.example.demospringboot.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "damaged_products")
public class DamagedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, length = 50)
    private String reason; // DAMAGED, EXPIRED, LOST

    @Column(name = "loss_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal lossAmount = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "reported_at", nullable = false)
    private LocalDate reportedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public DamagedProduct() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public BigDecimal getLossAmount() { return lossAmount; }
    public void setLossAmount(BigDecimal lossAmount) { this.lossAmount = lossAmount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDate reportedAt) { this.reportedAt = reportedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
