package com.billing.repository;

import com.billing.entity.Purchase;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Query("SELECT p FROM Purchase p JOIN FETCH p.product WHERE (:from IS NULL OR p.purchaseDate >= :from) AND (:to IS NULL OR p.purchaseDate <= :to) AND (:supplier IS NULL OR LOWER(p.supplierName) LIKE LOWER(CONCAT('%', :supplier, '%'))) AND (:productId IS NULL OR p.product.id = :productId) ORDER BY p.purchaseDate DESC, p.id DESC")
    List<Purchase> filter(@Param("from") LocalDate from, @Param("to") LocalDate to, @Param("supplier") String supplier, @Param("productId") Long productId);
    List<Purchase> findByExpiryDateBetween(LocalDate from, LocalDate to);
}
