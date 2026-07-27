package com.billing.dto;

import com.billing.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BillDTO {
    private Long id;
    private String billNumber;
    private String cashierName;
    private List<BillItemDTO> items;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal cgstAmount;
    private BigDecimal sgstAmount;
    private BigDecimal grandTotal;
    private PaymentMethod paymentMethod;
    private Integer totalItems;
    private LocalDateTime createdAt;

    public BillDTO() {}

    public BillDTO(Long id, String billNumber, String cashierName, List<BillItemDTO> items, BigDecimal subtotal, BigDecimal discountAmount, BigDecimal cgstAmount, BigDecimal sgstAmount, BigDecimal grandTotal, PaymentMethod paymentMethod, Integer totalItems, LocalDateTime createdAt) {
        this.id = id;
        this.billNumber = billNumber;
        this.cashierName = cashierName;
        this.items = items;
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.cgstAmount = cgstAmount;
        this.sgstAmount = sgstAmount;
        this.grandTotal = grandTotal;
        this.paymentMethod = paymentMethod;
        this.totalItems = totalItems;
        this.createdAt = createdAt;
    }

    public static BillDTOBuilder builder() {
        return new BillDTOBuilder();
    }

    public static class BillDTOBuilder {
        private Long id;
        private String billNumber;
        private String cashierName;
        private List<BillItemDTO> items;
        private BigDecimal subtotal;
        private BigDecimal discountAmount;
        private BigDecimal cgstAmount;
        private BigDecimal sgstAmount;
        private BigDecimal grandTotal;
        private PaymentMethod paymentMethod;
        private Integer totalItems;
        private LocalDateTime createdAt;

        BillDTOBuilder() {}

        public BillDTOBuilder id(Long id) { this.id = id; return this; }
        public BillDTOBuilder billNumber(String billNumber) { this.billNumber = billNumber; return this; }
        public BillDTOBuilder cashierName(String cashierName) { this.cashierName = cashierName; return this; }
        public BillDTOBuilder items(List<BillItemDTO> items) { this.items = items; return this; }
        public BillDTOBuilder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }
        public BillDTOBuilder discountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; return this; }
        public BillDTOBuilder cgstAmount(BigDecimal cgstAmount) { this.cgstAmount = cgstAmount; return this; }
        public BillDTOBuilder sgstAmount(BigDecimal sgstAmount) { this.sgstAmount = sgstAmount; return this; }
        public BillDTOBuilder grandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; return this; }
        public BillDTOBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public BillDTOBuilder totalItems(Integer totalItems) { this.totalItems = totalItems; return this; }
        public BillDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public BillDTO build() {
            return new BillDTO(id, billNumber, cashierName, items, subtotal, discountAmount, cgstAmount, sgstAmount, grandTotal, paymentMethod, totalItems, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public String getCashierName() { return cashierName; }
    public void setCashierName(String cashierName) { this.cashierName = cashierName; }

    public List<BillItemDTO> getItems() { return items; }
    public void setItems(List<BillItemDTO> items) { this.items = items; }

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
}