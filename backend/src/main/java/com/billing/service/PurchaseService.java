package com.billing.service;

import com.billing.dto.*;
import com.billing.entity.*;
import com.billing.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.*;
import java.util.*;

@Service
public class PurchaseService {
    private final PurchaseRepository purchases; private final ProductRepository products;
    public PurchaseService(PurchaseRepository purchases, ProductRepository products){this.purchases=purchases;this.products=products;}
    @Transactional public PurchaseDTO create(PurchaseRequest r) {
        Product product=products.findById(r.productId()).orElseThrow(()->new IllegalArgumentException("Product not found"));
        Purchase p=new Purchase(); p.setSupplierName(r.supplierName().trim()); p.setInvoiceNumber(r.invoiceNumber().trim()); p.setPurchaseDate(r.purchaseDate()); p.setProduct(product); p.setQuantity(r.quantity()); p.setPurchasePrice(r.purchasePrice()); p.setGstPercentage(r.gstPercentage()); p.setExpiryDate(r.expiryDate()); p.setBatchNumber(r.batchNumber());
        BigDecimal total=r.purchasePrice().multiply(BigDecimal.valueOf(r.quantity())).multiply(BigDecimal.ONE.add(r.gstPercentage().movePointLeft(2))).setScale(2, RoundingMode.HALF_UP); p.setTotalCost(total);
        product.setQuantity(product.getQuantity()+r.quantity()); product.setPurchasePrice(r.purchasePrice()); products.save(product); return map(purchases.save(p));
    }
    public List<PurchaseDTO> list(LocalDate from, LocalDate to, String supplier, Long productId){return purchases.filter(from,to,blankToNull(supplier),productId).stream().map(this::map).toList();}
    public List<InventoryAlertDTO> alerts(){
        List<InventoryAlertDTO> result=new ArrayList<>(); LocalDate near=LocalDate.now().plusDays(30);
        for(Product p:products.findByActiveTrue()) { if(p.getQuantity()==0) result.add(new InventoryAlertDTO("OUT_OF_STOCK","critical",p.getId(),p.getName(),p.getQuantity(),null,"Out of stock")); else if(p.getQuantity()<=p.getMinimumQuantity()) result.add(new InventoryAlertDTO("LOW_STOCK","warning",p.getId(),p.getName(),p.getQuantity(),null,"Below minimum stock")); }
        for(Purchase p:purchases.findByExpiryDateBetween(LocalDate.now(),near)) result.add(new InventoryAlertDTO("NEAR_EXPIRY", "warning", p.getProduct().getId(), p.getProduct().getName(), p.getQuantity(), p.getExpiryDate(), "Batch expires soon"));
        return result;
    }
    private PurchaseDTO map(Purchase p){return new PurchaseDTO(p.getId(),p.getSupplierName(),p.getInvoiceNumber(),p.getPurchaseDate(),p.getProduct().getId(),p.getProduct().getName(),p.getQuantity(),p.getPurchasePrice(),p.getGstPercentage(),p.getExpiryDate(),p.getBatchNumber(),p.getTotalCost());}
    private String blankToNull(String v){return v==null||v.isBlank()?null:v;}
}
