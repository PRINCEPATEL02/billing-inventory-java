package com.billing.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(unique = true)
    private String barcode;

    @Column(unique = true)
    private String qrCode;

    private String imageUrl;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal gstPercentage;

    private String hsnCode;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(nullable = false)
    private Integer minimumQuantity = 10;

    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Product() {}

    public Product(Long id, String name, String category, String barcode, String qrCode, String imageUrl, BigDecimal purchasePrice, BigDecimal sellingPrice, BigDecimal gstPercentage, String hsnCode, Integer quantity, Integer minimumQuantity, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
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
        private Integer quantity = 0;
        private Integer minimumQuantity = 10;
        private boolean active = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        ProductBuilder() {}

        public ProductBuilder id(Long id) { this.id = id; return this; }
        public ProductBuilder name(String name) { this.name = name; return this; }
        public ProductBuilder category(String category) { this.category = category; return this; }
        public ProductBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public ProductBuilder qrCode(String qrCode) { this.qrCode = qrCode; return this; }
        public ProductBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ProductBuilder purchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; return this; }
        public ProductBuilder sellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; return this; }
        public ProductBuilder gstPercentage(BigDecimal gstPercentage) { this.gstPercentage = gstPercentage; return this; }
        public ProductBuilder hsnCode(String hsnCode) { this.hsnCode = hsnCode; return this; }
        public ProductBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public ProductBuilder minimumQuantity(Integer minimumQuantity) { this.minimumQuantity = minimumQuantity; return this; }
        public ProductBuilder active(boolean active) { this.active = active; return this; }
        public ProductBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ProductBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Product build() {
            return new Product(id, name, category, barcode, qrCode, imageUrl, purchasePrice, sellingPrice, gstPercentage, hsnCode, quantity, minimumQuantity, active, createdAt, updatedAt);
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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}