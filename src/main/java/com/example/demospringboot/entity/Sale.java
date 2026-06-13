package com.example.demospringboot.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "paid_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "change_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal changeAmount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod = "CASH";

    @Column(length = 20)
    private String status = "COMPLETED";

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "sold_at")
    private LocalDateTime soldAt = LocalDateTime.now();

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SaleDetail> details = new ArrayList<>();

    public Sale() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public BigDecimal getChangeAmount() { return changeAmount; }
    public void setChangeAmount(BigDecimal changeAmount) { this.changeAmount = changeAmount; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getSoldAt() { return soldAt; }
    public void setSoldAt(LocalDateTime soldAt) { this.soldAt = soldAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<SaleDetail> getDetails() { return details; }
    public void setDetails(List<SaleDetail> details) { this.details = details; }
}
