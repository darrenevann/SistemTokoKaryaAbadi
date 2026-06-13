package com.example.demospringboot.repository;

import com.example.demospringboot.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findBySoldAtBetweenOrderBySoldAtDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.soldAt BETWEEN :start AND :end AND s.status = 'COMPLETED'")
    long countCompletedSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.soldAt BETWEEN :start AND :end AND s.status = 'COMPLETED'")
    BigDecimal sumTotalAmountBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(sd.subtotal - (sd.buyPrice * sd.quantity)), 0) FROM SaleDetail sd WHERE sd.sale.soldAt BETWEEN :start AND :end AND sd.sale.status = 'COMPLETED'")
    BigDecimal sumProfitBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT sd.product.id, sd.product.name, SUM(sd.quantity) as totalQty FROM SaleDetail sd WHERE sd.sale.soldAt BETWEEN :start AND :end AND sd.sale.status = 'COMPLETED' GROUP BY sd.product.id, sd.product.name ORDER BY totalQty DESC")
    List<Object[]> findTopProductsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT DATE(s.soldAt), COUNT(s), SUM(s.totalAmount) FROM Sale s WHERE s.soldAt BETWEEN :start AND :end AND s.status = 'COMPLETED' GROUP BY DATE(s.soldAt) ORDER BY DATE(s.soldAt)")
    List<Object[]> findDailySalesSummary(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Sale> findByUserIdOrderBySoldAtDesc(Long userId);
}
