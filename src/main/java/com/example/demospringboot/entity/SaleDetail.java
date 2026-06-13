package com.example.demospringboot.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_details")
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "sell_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal sellPrice;

    @Column(name = "buy_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal buyPrice;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public SaleDetail() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getSellPrice() { return sellPrice; }
    public void setSellPrice(BigDecimal sellPrice) { this.sellPrice = sellPrice; }

    public BigDecimal getBuyPrice() { return buyPrice; }
    public void setBuyPrice(BigDecimal buyPrice) { this.buyPrice = buyPrice; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
