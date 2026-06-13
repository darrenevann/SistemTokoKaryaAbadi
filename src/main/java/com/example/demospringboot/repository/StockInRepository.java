package com.example.demospringboot.repository;

import com.example.demospringboot.entity.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockInRepository extends JpaRepository<StockIn, Long> {

    List<StockIn> findByReceivedAtBetweenOrderByCreatedAtDesc(LocalDate startDate, LocalDate endDate);

    List<StockIn> findBySupplierId(Long supplierId);

    @Query("SELECT si FROM StockIn si ORDER BY si.createdAt DESC")
    List<StockIn> findAllOrderByCreatedAtDesc();

    @Query("SELECT COUNT(si) FROM StockIn si WHERE si.receivedAt = :date")
    long countByDate(@Param("date") LocalDate date);
}
