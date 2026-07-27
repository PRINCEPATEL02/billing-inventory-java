package com.billing.repository;

import com.billing.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByBarcode(String barcode);
    Optional<Product> findByQrCode(String qrCode);
    List<Product> findByActiveTrue();
    List<Product> findByActiveTrueAndQuantityLessThanEqual(Integer quantity);

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.quantity <= p.minimumQuantity")
    List<Product> findLowStockProducts();

    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.barcode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :search, '%')))" )
    List<Product> searchProducts(String search);

    @Query("SELECT SUM(p.quantity) FROM Product p WHERE p.active = true")
    Long sumCurrentStock();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.active = true AND p.quantity <= p.minimumQuantity")
    Long countLowStock();
}