package com.billing.entity;

import com.billing.enums.PaymentMethod;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String billNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User cashier;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillItem> items = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cgstAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sgstAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal grandTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private Integer totalItems;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Bill() {}

    public Bill(Long id, String billNumber, User cashier, List<BillItem> items, BigDecimal subtotal, BigDecimal discountAmount, BigDecimal cgstAmount, BigDecimal sgstAmount, BigDecimal grandTotal, PaymentMethod paymentMethod, Integer totalItems, LocalDateTime createdAt) {
        this.id = id;
        this.billNumber = billNumber;
        this.cashier = cashier;
        if (items != null) this.items = items;
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.cgstAmount = cgstAmount;
        this.sgstAmount = sgstAmount;
        this.grandTotal = grandTotal;
        this.paymentMethod = paymentMethod;
        this.totalItems = totalItems;
        this.createdAt = createdAt;
    }

    public static BillBuilder builder() {
        return new BillBuilder();
    }

    public static class BillBuilder {
        private Long id;
        private String billNumber;
        private User cashier;
        private List<BillItem> items = new ArrayList<>();
        private BigDecimal subtotal;
        private BigDecimal discountAmount;
        private BigDecimal cgstAmount;
        private BigDecimal sgstAmount;
        private BigDecimal grandTotal;
        private PaymentMethod paymentMethod;
        private Integer totalItems;
        private LocalDateTime createdAt;

        BillBuilder() {}

        public BillBuilder id(Long id) { this.id = id; return this; }
        public BillBuilder billNumber(String billNumber) { this.billNumber = billNumber; return this; }
        public BillBuilder cashier(User cashier) { this.cashier = cashier; return this; }
        public BillBuilder items(List<BillItem> items) { this.items = items; return this; }
        public BillBuilder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }
        public BillBuilder discountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; return this; }
        public BillBuilder cgstAmount(BigDecimal cgstAmount) { this.cgstAmount = cgstAmount; return this; }
        public BillBuilder sgstAmount(BigDecimal sgstAmount) { this.sgstAmount = sgstAmount; return this; }
        public BillBuilder grandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; return this; }
        public BillBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public BillBuilder totalItems(Integer totalItems) { this.totalItems = totalItems; return this; }
        public BillBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Bill build() {
            return new Bill(id, billNumber, cashier, items, subtotal, discountAmount, cgstAmount, sgstAmount, grandTotal, paymentMethod, totalItems, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public User getCashier() { return cashier; }
    public void setCashier(User cashier) { this.cashier = cashier; }

    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { this.items = items; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public BigDecimal getCgstAmount() { return cgstAmount; }
    public void setCgstAmount(BigDecimal cgstAmount) { this.cgstAmount = cgstAmount; }

    public BigDecimal getSgstAmount() { return sgstAmount; }
    public void setSgstAmount(BigDecimal sgstAmount) { this.sgstAmount = sgstAmount; }

    public BigDecimal getGrandTotal() { return grandTotal; }
    public void setGrandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void addItem(BillItem item) {
        items.add(item);
        item.setBill(this);
    }

    public void removeItem(BillItem item) {
        items.remove(item);
        item.setBill(null);
    }
}