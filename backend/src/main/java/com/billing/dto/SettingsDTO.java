package com.billing.dto;

public class SettingsDTO {
    private Long id;
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

    public SettingsDTO() {}

    public SettingsDTO(Long id, String companyName, String companyAddress, String companyPhone, String companyEmail, String gstNumber, String upiId, String invoiceLogoUrl, String invoiceSize, boolean darkMode, String printerSettings) {
        this.id = id;
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

    public static SettingsDTOBuilder builder() {
        return new SettingsDTOBuilder();
    }

    public static class SettingsDTOBuilder {
        private Long id;
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

        SettingsDTOBuilder() {}

        public SettingsDTOBuilder id(Long id) { this.id = id; return this; }
        public SettingsDTOBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public SettingsDTOBuilder companyAddress(String companyAddress) { this.companyAddress = companyAddress; return this; }
        public SettingsDTOBuilder companyPhone(String companyPhone) { this.companyPhone = companyPhone; return this; }
        public SettingsDTOBuilder companyEmail(String companyEmail) { this.companyEmail = companyEmail; return this; }
        public SettingsDTOBuilder gstNumber(String gstNumber) { this.gstNumber = gstNumber; return this; }
        public SettingsDTOBuilder upiId(String upiId) { this.upiId = upiId; return this; }
        public SettingsDTOBuilder invoiceLogoUrl(String invoiceLogoUrl) { this.invoiceLogoUrl = invoiceLogoUrl; return this; }
        public SettingsDTOBuilder invoiceSize(String invoiceSize) { this.invoiceSize = invoiceSize; return this; }
        public SettingsDTOBuilder darkMode(boolean darkMode) { this.darkMode = darkMode; return this; }
        public SettingsDTOBuilder printerSettings(String printerSettings) { this.printerSettings = printerSettings; return this; }

        public SettingsDTO build() {
            return new SettingsDTO(id, companyName, companyAddress, companyPhone, companyEmail, gstNumber, upiId, invoiceLogoUrl, invoiceSize, darkMode, printerSettings);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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