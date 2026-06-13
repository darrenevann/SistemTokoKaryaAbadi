package com.example.demospringboot.repository;

import com.example.demospringboot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByBarcode(String barcode);

    boolean existsByBarcode(String barcode);

    List<Product> findByIsActiveTrue();

    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR p.barcode LIKE CONCAT('%', :keyword, '%'))")
    List<Product> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.stock <= p.minStock")
    List<Product> findLowStockProducts();

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.stock = 0")
    List<Product> findOutOfStockProducts();
}
