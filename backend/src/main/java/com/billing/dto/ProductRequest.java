package com.billing.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    private String barcode;
    private String imageUrl;

    @NotNull(message = "Purchase price is required")
    @DecimalMin(value = "0.01")
    private BigDecimal purchasePrice;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.01")
    private BigDecimal sellingPrice;

    @NotNull(message = "GST percentage is required")
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private BigDecimal gstPercentage;

    private String hsnCode;

    @NotNull
    @Min(0)
    private Integer quantity;

    @NotNull
    @Min(0)
    private Integer minimumQuantity;

    public ProductRequest() {}

    public ProductRequest(String name, String category, String barcode, String imageUrl, BigDecimal purchasePrice, BigDecimal sellingPrice, BigDecimal gstPercentage, String hsnCode, Integer quantity, Integer minimumQuantity) {
        this.name = name;
        this.category = category;
        this.barcode = barcode;
        this.imageUrl = imageUrl;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.gstPercentage = gstPercentage;
        this.hsnCode = hsnCode;
        this.quantity = quantity;
        this.minimumQuantity = minimumQuantity;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public BigDecimal getGstPercentage() { return gstPercentage; }
    public void setGstPercentage(BigDecimal gstPercentage) { this.gstPercentage = gstPercentage; }

    public String getHsnCode() { return hsnCode; }
    public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getMinimumQuantity() { return minimumQuantity; }
    public void setMinimumQuantity(Integer minimumQuantity) { this.minimumQuantity = minimumQuantity; }
}