package com.billing.dto;
import java.math.BigDecimal; import java.time.LocalDateTime; import java.util.List;
public record BackupPayload(String generatedAt, List<ProductBackup> products, SettingsBackup settings) {
 public record ProductBackup(String name,String category,String barcode,BigDecimal purchasePrice,BigDecimal sellingPrice,BigDecimal gstPercentage,String hsnCode,Integer quantity,Integer minimumQuantity) {}
 public record SettingsBackup(String companyName,String companyAddress,String companyPhone,String companyEmail,String gstNumber,String invoiceLogoUrl,String invoiceSize,Boolean darkMode,String printerSettings) {}
}
