package com.billing.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bill_items")
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal gstPercentage;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cgstAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sgstAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    public BillItem() {}

    public BillItem(Long id, Bill bill, Product product, String productName, BigDecimal unitPrice, Integer quantity, BigDecimal gstPercentage, BigDecimal cgstAmount, BigDecimal sgstAmount, BigDecimal totalPrice) {
        this.id = id;
        this.bill = bill;
        this.product = product;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.gstPercentage = gstPercentage;
        this.cgstAmount = cgstAmount;
        this.sgstAmount = sgstAmount;
        this.totalPrice = totalPrice;
    }

    public static BillItemBuilder builder() {
        return new BillItemBuilder();
    }

    public static class BillItemBuilder {
        private Long id;
        private Bill bill;
        private Product product;
        private String productName;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal gstPercentage;
        private BigDecimal cgstAmount;
        private BigDecimal sgstAmount;
        private BigDecimal totalPrice;

        BillItemBuilder() {}

        public BillItemBuilder id(Long id) { this.id = id; return this; }
        public BillItemBuilder bill(Bill bill) { this.bill = bill; return this; }
        public BillItemBuilder product(Product product) { this.product = product; return this; }
        public BillItemBuilder productName(String productName) { this.productName = productName; return this; }
        public BillItemBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public BillItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public BillItemBuilder gstPercentage(BigDecimal gstPercentage) { this.gstPercentage = gstPercentage; return this; }
        public BillItemBuilder cgstAmount(BigDecimal cgstAmount) { this.cgstAmount = cgstAmount; return this; }
        public BillItemBuilder sgstAmount(BigDecimal sgstAmount) { this.sgstAmount = sgstAmount; return this; }
        public BillItemBuilder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }

        public BillItem build() {
            return new BillItem(id, bill, product, productName, unitPrice, quantity, gstPercentage, cgstAmount, sgstAmount, totalPrice);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getGstPercentage() { return gstPercentage; }
    public void setGstPercentage(BigDecimal gstPercentage) { this.gstPercentage = gstPercentage; }

    public BigDecimal getCgstAmount() { return cgstAmount; }
    public void setCgstAmount(BigDecimal cgstAmount) { this.cgstAmount = cgstAmount; }

    public BigDecimal getSgstAmount() { return sgstAmount; }
    public void setSgstAmount(BigDecimal sgstAmount) { this.sgstAmount = sgstAmount; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
}