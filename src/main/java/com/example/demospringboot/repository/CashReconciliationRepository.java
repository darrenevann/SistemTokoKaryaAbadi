package com.example.demospringboot.repository;

import com.example.demospringboot.entity.CashReconciliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CashReconciliationRepository extends JpaRepository<CashReconciliation, Long> {

    Optional<CashReconciliation> findByReconciliationDate(LocalDate date);

    List<CashReconciliation> findByReconciliationDateBetweenOrderByReconciliationDateDesc(LocalDate start, LocalDate end);

    List<CashReconciliation> findAllByOrderByReconciliationDateDesc();

    @Query("SELECT COALESCE(SUM(cr.actualCash), 0) FROM CashReconciliation cr WHERE cr.reconciliationDate BETWEEN :start AND :end")
    BigDecimal sumActualCashBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
