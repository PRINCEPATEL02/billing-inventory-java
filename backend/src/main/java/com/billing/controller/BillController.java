package com.billing.controller;

import com.billing.dto.*;
import com.billing.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@Tag(name = "Billing", description = "POS Billing APIs")
@SecurityRequirement(name = "bearerAuth")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    @Operation(summary = "Create new bill")
    public ResponseEntity<ApiResponse<BillDTO>> createBill(
            @Valid @RequestBody BillRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Bill created successfully", 
                billService.createBill(request, userDetails.getUsername())));
    }

    @GetMapping
    @Operation(summary = "Get all bills")
    public ResponseEntity<ApiResponse<List<BillDTO>>> getAllBills() {
        return ResponseEntity.ok(ApiResponse.success(billService.getAllBills()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get bill by ID")
    public ResponseEntity<ApiResponse<BillDTO>> getBill(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(billService.getBillById(id)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search bills")
    public ResponseEntity<ApiResponse<List<BillDTO>>> search(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.success(billService.searchBills(q)));
    }

    @GetMapping("/{id}/qr")
    @Operation(summary = "Get verification QR code for bill")
    public ResponseEntity<ApiResponse<String>> getBillQrCode(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(billService.generateBillVerificationQRCode(id)));
    }

    @GetMapping("/upi-qr")
    @Operation(summary = "Generate dynamic UPI payment QR code")
    public ResponseEntity<ApiResponse<String>> getUpiPaymentQrCode(
            @RequestParam java.math.BigDecimal amount,
            @RequestParam(required = false, defaultValue = "POS_PAYMENT") String note) {
        return ResponseEntity.ok(ApiResponse.success(billService.generateUPIPaymentQRCode(amount, note)));
    }
}