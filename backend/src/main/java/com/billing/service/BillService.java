package com.billing.service;

import com.billing.dto.*;
import com.billing.entity.Bill;
import com.billing.entity.BillItem;
import com.billing.entity.Product;
import com.billing.entity.User;
import com.billing.repository.BillRepository;
import com.billing.repository.ProductRepository;
import com.billing.repository.UserRepository;
import com.billing.util.QRCodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final QRCodeGenerator qrCodeGenerator;
    private final SettingsService settingsService;

    public BillService(BillRepository billRepository, ProductRepository productRepository, UserRepository userRepository, QRCodeGenerator qrCodeGenerator, SettingsService settingsService) {
        this.billRepository = billRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.qrCodeGenerator = qrCodeGenerator;
        this.settingsService = settingsService;
    }

    @Transactional
    public BillDTO createBill(BillRequest request, String username) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Bill must contain at least one item");
        }

        User cashier = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Cashier not found"));

        Bill bill = new Bill();
        bill.setBillNumber(generateBillNumber());
        bill.setCashier(cashier);
        bill.setDiscountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO);
        bill.setPaymentMethod(request.getPaymentMethod());

        BigDecimal subtotal = BigDecimal.ZERO;
        int totalItems = 0;

        for (BillItemRequest itemReq : request.getItems()) {
            if (itemReq.getQuantity() <= 0) {
                throw new IllegalArgumentException("Item quantity must be greater than zero");
            }
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));

            if (product.getQuantity() < itemReq.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName() + " (Available: " + product.getQuantity() + ")");
            }

            BigDecimal itemTotal = product.getSellingPrice()
                    .multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            BigDecimal gstAmount = itemTotal.multiply(product.getGstPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal cgst = gstAmount.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
            BigDecimal sgst = cgst;

            BillItem item = BillItem.builder()
                    .product(product)
                    .productName(product.getName())
                    .unitPrice(product.getSellingPrice())
                    .quantity(itemReq.getQuantity())
                    .gstPercentage(product.getGstPercentage())
                    .cgstAmount(cgst)
                    .sgstAmount(sgst)
                    .totalPrice(itemTotal.add(gstAmount))
                    .build();

            bill.addItem(item);
            subtotal = subtotal.add(itemTotal);
            totalItems += itemReq.getQuantity();

            product.setQuantity(product.getQuantity() - itemReq.getQuantity());
            productRepository.save(product);
        }

        BigDecimal discount = bill.getDiscountAmount();
        BigDecimal taxableAmount = subtotal.subtract(discount).max(BigDecimal.ZERO);
        BigDecimal totalGst = bill.getItems().stream()
                .map(BillItem::getCgstAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.valueOf(2));

        bill.setSubtotal(subtotal);
        bill.setCgstAmount(totalGst.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
        bill.setSgstAmount(totalGst.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
        bill.setGrandTotal(taxableAmount.add(totalGst));
        bill.setTotalItems(totalItems);

        billRepository.save(bill);
        return mapToDTO(bill);
    }

    public List<BillDTO> getAllBills() {
        return billRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public BillDTO getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found: " + id));
        return mapToDTO(bill);
    }

    public List<BillDTO> searchBills(String search) {
        return billRepository.searchBills(search).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public String generateBillVerificationQRCode(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found: " + billId));

        String qrContent = String.format("INVOICE|No:%s|Date:%s|Total:INR %.2f|Items:%d|Cashier:%s",
                bill.getBillNumber(),
                bill.getCreatedAt() != null ? bill.getCreatedAt().toString() : "",
                bill.getGrandTotal(),
                bill.getTotalItems(),
                bill.getCashier() != null ? bill.getCashier().getFullName() : "N/A");

        return qrCodeGenerator.generateQRCode(qrContent, 250, 250);
    }

    public String generateUPIPaymentQRCode(BigDecimal amount, String note) {
        SettingsDTO settings = settingsService.getSettings();
        String upiId = (settings.getUpiId() != null && !settings.getUpiId().isBlank())
                ? settings.getUpiId() : "store@upi";
        String companyName = settings.getCompanyName() != null ? settings.getCompanyName() : "Store";

        String cleanNote = note != null ? note.replaceAll("[^a-zA-Z0-9_-]", "") : "InvoicePayment";
        BigDecimal safeAmount = amount != null ? amount.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        String upiUrl = String.format("upi://pay?pa=%s&pn=%s&am=%s&cu=INR&tn=%s",
                upiId,
                companyName.replace(" ", "%20"),
                safeAmount.toString(),
                cleanNote);

        return qrCodeGenerator.generateQRCode(upiUrl, 300, 300);
    }

    private String generateBillNumber() {
        return "BILL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private BillDTO mapToDTO(Bill bill) {
        return BillDTO.builder()
                .id(bill.getId())
                .billNumber(bill.getBillNumber())
                .cashierName(bill.getCashier() != null ? bill.getCashier().getFullName() : "System")
                .items(bill.getItems() != null ? bill.getItems().stream().map(item -> BillItemDTO.builder()
                        .id(item.getId())
                        .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                        .productName(item.getProductName())
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .gstPercentage(item.getGstPercentage())
                        .cgstAmount(item.getCgstAmount())
                        .sgstAmount(item.getSgstAmount())
                        .totalPrice(item.getTotalPrice())
                        .build()).collect(Collectors.toList()) : List.of())
                .subtotal(bill.getSubtotal())
                .discountAmount(bill.getDiscountAmount())
                .cgstAmount(bill.getCgstAmount())
                .sgstAmount(bill.getSgstAmount())
                .grandTotal(bill.getGrandTotal())
                .paymentMethod(bill.getPaymentMethod())
                .totalItems(bill.getTotalItems())
                .createdAt(bill.getCreatedAt())
                .build();
    }
}