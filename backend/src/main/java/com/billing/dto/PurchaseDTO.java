package com.billing.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
public record PurchaseDTO(Long id, String supplierName, String invoiceNumber, LocalDate purchaseDate, Long productId,
    String productName, Integer quantity, BigDecimal purchasePrice, BigDecimal gstPercentage, LocalDate expiryDate,
    String batchNumber, BigDecimal totalCost) {}
