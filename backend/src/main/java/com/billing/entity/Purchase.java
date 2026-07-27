package com.billing.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases", indexes = {@Index(name = "idx_purchase_date", columnList = "purchaseDate"), @Index(name = "idx_purchase_supplier", columnList = "supplierName")})
public class Purchase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String supplierName;
    @Column(nullable = false) private String invoiceNumber;
    @Column(nullable = false) private LocalDate purchaseDate;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id", nullable = false) private Product product;
    @Column(nullable = false) private Integer quantity;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal purchasePrice;
    @Column(nullable = false, precision = 5, scale = 2) private BigDecimal gstPercentage;
    private LocalDate expiryDate;
    private String batchNumber;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal totalCost;
    @CreationTimestamp private LocalDateTime createdAt;

    public Long getId(){return id;} public String getSupplierName(){return supplierName;} public void setSupplierName(String v){supplierName=v;}
    public String getInvoiceNumber(){return invoiceNumber;} public void setInvoiceNumber(String v){invoiceNumber=v;}
    public LocalDate getPurchaseDate(){return purchaseDate;} public void setPurchaseDate(LocalDate v){purchaseDate=v;}
    public Product getProduct(){return product;} public void setProduct(Product v){product=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public BigDecimal getPurchasePrice(){return purchasePrice;} public void setPurchasePrice(BigDecimal v){purchasePrice=v;}
    public BigDecimal getGstPercentage(){return gstPercentage;} public void setGstPercentage(BigDecimal v){gstPercentage=v;}
    public LocalDate getExpiryDate(){return expiryDate;} public void setExpiryDate(LocalDate v){expiryDate=v;}
    public String getBatchNumber(){return batchNumber;} public void setBatchNumber(String v){batchNumber=v;}
    public BigDecimal getTotalCost(){return totalCost;} public void setTotalCost(BigDecimal v){totalCost=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
