package com.billing.dto;

import jakarta.validation.constraints.NotBlank;

public class SettingsRequest {
    @NotBlank(message = "Company name is required")
    private String companyName;

    private String companyAddress;
    private String companyPhone;
    private String companyEmail;
    private String gstNumber;
    private String upiId;
    private String invoiceLogoUrl;
    private String invoiceSize;
    private boolean darkMode;
    private String printerSettings;

    public SettingsRequest() {}

    public SettingsRequest(String companyName, String companyAddress, String companyPhone, String companyEmail, String gstNumber, String upiId, String invoiceLogoUrl, String invoiceSize, boolean darkMode, String printerSettings) {
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyPhone = companyPhone;
        this.companyEmail = companyEmail;
        this.gstNumber = gstNumber;
        this.upiId = upiId;
        this.invoiceLogoUrl = invoiceLogoUrl;
        this.invoiceSize = invoiceSize;
        this.darkMode = darkMode;
        this.printerSettings = printerSettings;
    }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCompanyAddress() { return companyAddress; }
    public void setCompanyAddress(String companyAddress) { this.companyAddress = companyAddress; }

    public String getCompanyPhone() { return companyPhone; }
    public void setCompanyPhone(String companyPhone) { this.companyPhone = companyPhone; }

    public String getCompanyEmail() { return companyEmail; }
    public void setCompanyEmail(String companyEmail) { this.companyEmail = companyEmail; }

    public String getGstNumber() { return gstNumber; }
    public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }

    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }

    public String getInvoiceLogoUrl() { return invoiceLogoUrl; }
    public void setInvoiceLogoUrl(String invoiceLogoUrl) { this.invoiceLogoUrl = invoiceLogoUrl; }

    public String getInvoiceSize() { return invoiceSize; }
    public void setInvoiceSize(String invoiceSize) { this.invoiceSize = invoiceSize; }

    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }

    public String getPrinterSettings() { return printerSettings; }
    public void setPrinterSettings(String printerSettings) { this.printerSettings = printerSettings; }
}