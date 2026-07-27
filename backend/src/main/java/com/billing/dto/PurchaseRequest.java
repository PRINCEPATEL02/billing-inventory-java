package com.billing.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record PurchaseRequest(@NotBlank String supplierName, @NotBlank String invoiceNumber, @NotNull LocalDate purchaseDate,
    @NotNull Long productId, @NotNull @Min(1) Integer quantity, @NotNull @DecimalMin("0.00") BigDecimal purchasePrice,
    @NotNull @DecimalMin("0.00") BigDecimal gstPercentage, LocalDate expiryDate, String batchNumber) {}
