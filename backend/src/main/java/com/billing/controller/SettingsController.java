package com.billing.controller;

import com.billing.dto.*;
import com.billing.service.SettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@Tag(name = "Settings", description = "Application settings APIs")
@SecurityRequirement(name = "bearerAuth")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    @Operation(summary = "Get settings")
    public ResponseEntity<ApiResponse<SettingsDTO>> getSettings() {
        return ResponseEntity.ok(ApiResponse.success(settingsService.getSettings()));
    }

    @PutMapping
    @Operation(summary = "Update settings")
    public ResponseEntity<ApiResponse<SettingsDTO>> updateSettings(@Valid @RequestBody SettingsRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Settings updated", settingsService.updateSettings(request)));
    }
}