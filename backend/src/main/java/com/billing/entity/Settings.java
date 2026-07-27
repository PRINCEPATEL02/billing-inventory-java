package com.billing.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    private String companyAddress;
    private String companyPhone;
    private String companyEmail;
    private String gstNumber;
    private String upiId = "store@upi";

    private String invoiceLogoUrl;

    @Column(nullable = false)
    private String invoiceSize = "A4";

    @Column(nullable = false)
    private boolean darkMode = false;

    private String printerSettings;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Settings() {}

    public Settings(Long id, String companyName, String companyAddress, String companyPhone, String companyEmail, String gstNumber, String upiId, String invoiceLogoUrl, String invoiceSize, boolean darkMode, String printerSettings, LocalDateTime updatedAt) {
        this.id = id;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyPhone = companyPhone;
        this.companyEmail = companyEmail;
        this.gstNumber = gstNumber;
        this.upiId = upiId != null ? upiId : "store@upi";
        this.invoiceLogoUrl = invoiceLogoUrl;
        this.invoiceSize = invoiceSize;
        this.darkMode = darkMode;
        this.printerSettings = printerSettings;
        this.updatedAt = updatedAt;
    }

    public static SettingsBuilder builder() {
        return new SettingsBuilder();
    }

    public static class SettingsBuilder {
        private Long id;
        private String companyName;
        private String companyAddress;
        private String companyPhone;
        private String companyEmail;
        private String gstNumber;
        private String upiId = "store@upi";
        private String invoiceLogoUrl;
        private String invoiceSize = "A4";
        private boolean darkMode = false;
        private String printerSettings;
        private LocalDateTime updatedAt;

        SettingsBuilder() {}

        public SettingsBuilder id(Long id) { this.id = id; return this; }
        public SettingsBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public SettingsBuilder companyAddress(String companyAddress) { this.companyAddress = companyAddress; return this; }
        public SettingsBuilder companyPhone(String companyPhone) { this.companyPhone = companyPhone; return this; }
        public SettingsBuilder companyEmail(String companyEmail) { this.companyEmail = companyEmail; return this; }
        public SettingsBuilder gstNumber(String gstNumber) { this.gstNumber = gstNumber; return this; }
        public SettingsBuilder upiId(String upiId) { this.upiId = upiId; return this; }
        public SettingsBuilder invoiceLogoUrl(String invoiceLogoUrl) { this.invoiceLogoUrl = invoiceLogoUrl; return this; }
        public SettingsBuilder invoiceSize(String invoiceSize) { this.invoiceSize = invoiceSize; return this; }
        public SettingsBuilder darkMode(boolean darkMode) { this.darkMode = darkMode; return this; }
        public SettingsBuilder printerSettings(String printerSettings) { this.printerSettings = printerSettings; return this; }
        public SettingsBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Settings build() {
            return new Settings(id, companyName, companyAddress, companyPhone, companyEmail, gstNumber, upiId, invoiceLogoUrl, invoiceSize, darkMode, printerSettings, updatedAt);
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

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}