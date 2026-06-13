package com.example.demospringboot.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_reconciliation")
public class CashReconciliation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reconciliation_date", nullable = false)
    private LocalDate reconciliationDate;

    @Column(name = "expected_cash", nullable = false, precision = 15, scale = 2)
    private BigDecimal expectedCash = BigDecimal.ZERO;

    @Column(name = "actual_cash", nullable = false, precision = 15, scale = 2)
    private BigDecimal actualCash = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal difference = BigDecimal.ZERO;

    @Column(length = 20)
    private String status = "PENDING"; // MATCH, OVER, SHORT

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public CashReconciliation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getReconciliationDate() { return reconciliationDate; }
    public void setReconciliationDate(LocalDate reconciliationDate) { this.reconciliationDate = reconciliationDate; }

    public BigDecimal getExpectedCash() { return expectedCash; }
    public void setExpectedCash(BigDecimal expectedCash) { this.expectedCash = expectedCash; }

    public BigDecimal getActualCash() { return actualCash; }
    public void setActualCash(BigDecimal actualCash) { this.actualCash = actualCash; }

    public BigDecimal getDifference() { return difference; }
    public void setDifference(BigDecimal difference) { this.difference = difference; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
