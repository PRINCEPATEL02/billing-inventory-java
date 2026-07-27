package com.billing.controller;

import com.billing.dto.*;
import com.billing.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product management APIs")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.success(productService.getAllProducts()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<ProductDTO>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductById(id)));
    }

    @GetMapping("/barcode/{barcode}")
    @Operation(summary = "Get product by barcode")
    public ResponseEntity<ApiResponse<ProductDTO>> getByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductByBarcode(barcode)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> search(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.success(productService.searchProducts(q)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create product (Admin only)")
    public ResponseEntity<ApiResponse<ProductDTO>> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Product created", productService.createProduct(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product (Admin only)")
    public ResponseEntity<ApiResponse<ProductDTO>> update(@PathVariable Long id, 
                                                           @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Product updated", productService.updateProduct(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete product (Admin only)")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted", null));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getLowStock() {
        return ResponseEntity.ok(ApiResponse.success(productService.getLowStockProducts()));
    }

    @GetMapping("/{id}/qr")
    @Operation(summary = "Get product QR code")
    public ResponseEntity<ApiResponse<String>> getProductQrCode(@PathVariable Long id) {
        String qrCode = productService.getOrGenerateProductQRCode(id);
        return ResponseEntity.ok(ApiResponse.success("QR code generated", qrCode));
    }

    @PostMapping("/generate-all-qr")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Bulk generate QR codes for products (Admin only)")
    public ResponseEntity<ApiResponse<Integer>> generateAllQRCodes() {
        int count = productService.generateAllMissingQRCodes();
        return ResponseEntity.ok(ApiResponse.success("Generated QR codes for " + count + " products", count));
    }
}