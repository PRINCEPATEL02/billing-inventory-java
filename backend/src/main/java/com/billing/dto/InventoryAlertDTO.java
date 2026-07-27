package com.billing.dto;
import java.time.LocalDate;
public record InventoryAlertDTO(String type, String severity, Long productId, String productName, Integer quantity, LocalDate expiryDate, String message) {}
