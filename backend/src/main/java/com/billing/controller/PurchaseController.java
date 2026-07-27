package com.billing.controller;
import com.billing.dto.*; import com.billing.service.PurchaseService; import jakarta.validation.Valid; import org.springframework.http.ResponseEntity; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.time.LocalDate; import java.util.List;
@RestController @RequestMapping("/api/purchases") public class PurchaseController {
 private final PurchaseService service; public PurchaseController(PurchaseService service){this.service=service;}
 @GetMapping public ResponseEntity<ApiResponse<List<PurchaseDTO>>> list(@RequestParam(required=false) LocalDate from,@RequestParam(required=false) LocalDate to,@RequestParam(required=false) String supplier,@RequestParam(required=false) Long productId){return ResponseEntity.ok(ApiResponse.success(service.list(from,to,supplier,productId)));}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<PurchaseDTO>> create(@Valid @RequestBody PurchaseRequest request){return ResponseEntity.ok(ApiResponse.success("Purchase completed and stock updated",service.create(request)));}
 @GetMapping("/alerts") public ResponseEntity<ApiResponse<List<InventoryAlertDTO>>> alerts(){return ResponseEntity.ok(ApiResponse.success(service.alerts()));}
}
