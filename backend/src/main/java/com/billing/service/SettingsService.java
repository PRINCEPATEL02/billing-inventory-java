package com.billing.service;

import com.billing.dto.SettingsDTO;
import com.billing.dto.SettingsRequest;
import com.billing.entity.Settings;
import com.billing.repository.SettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;

    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Transactional(readOnly = true)
    public SettingsDTO getSettings() {
        Settings settings = settingsRepository.findFirstByOrderByIdAsc()
                .orElseGet(this::createDefaultSettings);
        return mapToDTO(settings);
    }

    @Transactional
    public SettingsDTO updateSettings(SettingsRequest request) {
        Settings settings = settingsRepository.findFirstByOrderByIdAsc()
                .orElseGet(this::createDefaultSettings);

        if (request.getCompanyName() != null && !request.getCompanyName().isBlank()) {
            settings.setCompanyName(request.getCompanyName());
        }
        
        settings.setCompanyAddress(request.getCompanyAddress());
        settings.setCompanyPhone(request.getCompanyPhone());
        settings.setCompanyEmail(request.getCompanyEmail());
        settings.setGstNumber(request.getGstNumber());
        
        if (request.getUpiId() != null && !request.getUpiId().isBlank()) {
            settings.setUpiId(request.getUpiId());
        } else if (settings.getUpiId() == null || settings.getUpiId().isBlank()) {
            settings.setUpiId("store@upi");
        }

        settings.setInvoiceLogoUrl(request.getInvoiceLogoUrl());
        
        if (request.getInvoiceSize() != null && !request.getInvoiceSize().isBlank()) {
            settings.setInvoiceSize(request.getInvoiceSize());
        } else if (settings.getInvoiceSize() == null) {
            settings.setInvoiceSize("A4");
        }

        settings.setDarkMode(request.isDarkMode());
        settings.setPrinterSettings(request.getPrinterSettings());

        settingsRepository.save(settings);
        return mapToDTO(settings);
    }

    @Transactional
    private Settings createDefaultSettings() {
        return settingsRepository.save(Settings.builder()
                .companyName("My Store")
                .upiId("store@upi")
                .invoiceSize("A4")
                .darkMode(false)
                .build());
    }

    private SettingsDTO mapToDTO(Settings settings) {
        return SettingsDTO.builder()
                .id(settings.getId())
                .companyName(settings.getCompanyName())
                .companyAddress(settings.getCompanyAddress())
                .companyPhone(settings.getCompanyPhone())
                .companyEmail(settings.getCompanyEmail())
                .gstNumber(settings.getGstNumber())
                .upiId(settings.getUpiId() != null ? settings.getUpiId() : "store@upi")
                .invoiceLogoUrl(settings.getInvoiceLogoUrl())
                .invoiceSize(settings.getInvoiceSize())
                .darkMode(settings.isDarkMode())
                .printerSettings(settings.getPrinterSettings())
                .build();
    }
}