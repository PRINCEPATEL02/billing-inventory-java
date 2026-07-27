package com.billing.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductDTO {
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    private String barcode;
    private String qrCode;
    private String imageUrl;

    @NotNull(message = "Purchase price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal purchasePrice;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal sellingPrice;

    @NotNull(message = "GST percentage is required")
    @DecimalMin(value = "0", message = "GST cannot be negative")
    @DecimalMax(value = "100", message = "GST cannot exceed 100")
    private BigDecimal gstPercentage;

    private String hsnCode;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Minimum quantity is required")
    @Min(value = 0, message = "Minimum quantity cannot be negative")
    private Integer minimumQuantity;

    private boolean active;
    private boolean lowStock;

    public ProductDTO() {}

    public ProductDTO(Long id, String name, String category, String barcode, String qrCode, String imageUrl, BigDecimal purchasePrice, BigDecimal sellingPrice, BigDecimal gstPercentage, String hsnCode, Integer quantity, Integer minimumQuantity, boolean active, boolean lowStock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.barcode = barcode;
        this.qrCode = qrCode;
        this.imageUrl = imageUrl;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.gstPercentage = gstPercentage;
        this.hsnCode = hsnCode;
        this.quantity = quantity;
        this.minimumQuantity = minimumQuantity;
        this.active = active;
        this.lowStock = lowStock;
    }

    public static ProductDTOBuilder builder() {
        return new ProductDTOBuilder();
    }

    public static class ProductDTOBuilder {
        private Long id;
        private String name;
        private String category;
        private String barcode;
        private String qrCode;
        private String imageUrl;
        private BigDecimal purchasePrice;
        private BigDecimal sellingPrice;
        private BigDecimal gstPercentage;
        private String hsnCode;
        private Integer quantity;
        private Integer minimumQuantity;
        private boolean active;
        private boolean lowStock;

        ProductDTOBuilder() {}

        public ProductDTOBuilder id(Long id) { this.id = id; return this; }
        public ProductDTOBuilder name(String name) { this.name = name; return this; }
        public ProductDTOBuilder category(String category) { this.category = category; return this; }
        public ProductDTOBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public ProductDTOBuilder qrCode(String qrCode) { this.qrCode = qrCode; return this; }
        public ProductDTOBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ProductDTOBuilder purchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; return this; }
        public ProductDTOBuilder sellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; return this; }
        public ProductDTOBuilder gstPercentage(BigDecimal gstPercentage) { this.gstPercentage = gstPercentage; return this; }
        public ProductDTOBuilder hsnCode(String hsnCode) { this.hsnCode = hsnCode; return this; }
        public ProductDTOBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public ProductDTOBuilder minimumQuantity(Integer minimumQuantity) { this.minimumQuantity = minimumQuantity; return this; }
        public ProductDTOBuilder active(boolean active) { this.active = active; return this; }
        public ProductDTOBuilder lowStock(boolean lowStock) { this.lowStock = lowStock; return this; }

        public ProductDTO build() {
            return new ProductDTO(id, name, category, barcode, qrCode, imageUrl, purchasePrice, sellingPrice, gstPercentage, hsnCode, quantity, minimumQuantity, active, lowStock);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

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

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isLowStock() { return lowStock; }
    public void setLowStock(boolean lowStock) { this.lowStock = lowStock; }
}