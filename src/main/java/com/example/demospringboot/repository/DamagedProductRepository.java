package com.example.demospringboot.repository;

import com.example.demospringboot.entity.DamagedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DamagedProductRepository extends JpaRepository<DamagedProduct, Long> {

    List<DamagedProduct> findByReportedAtBetweenOrderByReportedAtDesc(LocalDate start, LocalDate end);

    List<DamagedProduct> findByProductId(Long productId);

    @Query("SELECT COALESCE(SUM(dp.lossAmount), 0) FROM DamagedProduct dp WHERE dp.reportedAt BETWEEN :start AND :end")
    BigDecimal sumLossAmountBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    List<DamagedProduct> findByReason(String reason);

    List<DamagedProduct> findAllByOrderByReportedAtDesc();
}
