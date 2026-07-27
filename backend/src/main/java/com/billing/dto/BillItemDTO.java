package com.billing.dto;

import java.math.BigDecimal;

public class BillItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal gstPercentage;
    private BigDecimal cgstAmount;
    private BigDecimal sgstAmount;
    private BigDecimal totalPrice;

    public BillItemDTO() {}

    public BillItemDTO(Long id, Long productId, String productName, BigDecimal unitPrice, Integer quantity, BigDecimal gstPercentage, BigDecimal cgstAmount, BigDecimal sgstAmount, BigDecimal totalPrice) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.gstPercentage = gstPercentage;
        this.cgstAmount = cgstAmount;
        this.sgstAmount = sgstAmount;
        this.totalPrice = totalPrice;
    }

    public static BillItemDTOBuilder builder() {
        return new BillItemDTOBuilder();
    }

    public static class BillItemDTOBuilder {
        private Long id;
        private Long productId;
        private String productName;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal gstPercentage;
        private BigDecimal cgstAmount;
        private BigDecimal sgstAmount;
        private BigDecimal totalPrice;

        BillItemDTOBuilder() {}

        public BillItemDTOBuilder id(Long id) { this.id = id; return this; }
        public BillItemDTOBuilder productId(Long productId) { this.productId = productId; return this; }
        public BillItemDTOBuilder productName(String productName) { this.productName = productName; return this; }
        public BillItemDTOBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public BillItemDTOBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public BillItemDTOBuilder gstPercentage(BigDecimal gstPercentage) { this.gstPercentage = gstPercentage; return this; }
        public BillItemDTOBuilder cgstAmount(BigDecimal cgstAmount) { this.cgstAmount = cgstAmount; return this; }
        public BillItemDTOBuilder sgstAmount(BigDecimal sgstAmount) { this.sgstAmount = sgstAmount; return this; }
        public BillItemDTOBuilder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }

        public BillItemDTO build() {
            return new BillItemDTO(id, productId, productName, unitPrice, quantity, gstPercentage, cgstAmount, sgstAmount, totalPrice);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

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