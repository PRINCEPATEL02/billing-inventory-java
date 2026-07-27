package com.billing.dto;

import com.billing.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public class BillRequest {
    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<BillItemRequest> items;

    @NotNull(message = "Discount amount is required")
    @DecimalMin(value = "0")
    private BigDecimal discountAmount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    public BillRequest() {}

    public BillRequest(List<BillItemRequest> items, BigDecimal discountAmount, PaymentMethod paymentMethod) {
        this.items = items;
        this.discountAmount = discountAmount;
        this.paymentMethod = paymentMethod;
    }

    public List<BillItemRequest> getItems() { return items; }
    public void setItems(List<BillItemRequest> items) { this.items = items; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
}