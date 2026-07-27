package com.billing.controller;

import com.billing.dto.ApiResponse;
import com.billing.util.QRCodeGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/qrcode")
public class QRCodeController {

    private final QRCodeGenerator qrCodeGenerator;

    public QRCodeController(QRCodeGenerator qrCodeGenerator) {
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @GetMapping("/generate")
    public ResponseEntity<ApiResponse<Map<String, String>>> generateQRCode(
            @RequestParam String text,
            @RequestParam(defaultValue = "250") int width,
            @RequestParam(defaultValue = "250") int height) {
        String base64Qr = qrCodeGenerator.generateQRCode(text, width, height);
        return ResponseEntity.ok(ApiResponse.success("QR code generated", Map.of("qrCode", base64Qr, "text", text)));
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> generateQRCodeImage(
            @RequestParam String text,
            @RequestParam(defaultValue = "300") int width,
            @RequestParam(defaultValue = "300") int height) {
        byte[] imageBytes = qrCodeGenerator.generateQRCodeImage(text, width, height);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(imageBytes);
    }
}
