package com.billing.seed;

import com.billing.entity.Product;
import com.billing.entity.Settings;
import com.billing.entity.User;
import com.billing.enums.Role;
import com.billing.repository.ProductRepository;
import com.billing.repository.SettingsRepository;
import com.billing.repository.UserRepository;
import com.billing.util.QRCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SettingsRepository settingsRepository;
    private final PasswordEncoder passwordEncoder;
    private final QRCodeGenerator qrCodeGenerator;

    public DataSeeder(UserRepository userRepository, ProductRepository productRepository, SettingsRepository settingsRepository, PasswordEncoder passwordEncoder, QRCodeGenerator qrCodeGenerator) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.settingsRepository = settingsRepository;
        this.passwordEncoder = passwordEncoder;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        seedProducts();
        seedSettings();
        log.info("Seed data loaded successfully");
    }

    private void seedAdmin() {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Administrator")
                    .email("admin@billing.com")
                    .role(Role.ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);

            User employee = User.builder()
                    .username("cashier")
                    .password(passwordEncoder.encode("cashier123"))
                    .fullName("John Cashier")
                    .email("cashier@billing.com")
                    .role(Role.EMPLOYEE)
                    .active(true)
                    .build();
            userRepository.save(employee);
            log.info("Default users created");
        }
    }

    private void seedProducts() {
        if (productRepository.count() == 0) {
            String[][] products = {
                {"Rice 5kg", "Groceries", "8901234567890", "150.00", "180.00", "5.00", "1001", "50", "10"},
                {"Wheat Flour 1kg", "Groceries", "8901234567891", "40.00", "55.00", "5.00", "1002", "100", "20"},
                {"Sugar 1kg", "Groceries", "8901234567892", "35.00", "45.00", "5.00", "1003", "80", "15"},
                {"Milk 1L", "Dairy", "8901234567893", "50.00", "65.00", "12.00", "1004", "30", "10"},
                {"Bread", "Bakery", "8901234567894", "25.00", "35.00", "5.00", "1005", "20", "5"},
                {"Tea Powder 250g", "Beverages", "8901234567895", "80.00", "120.00", "18.00", "1006", "40", "10"},
                {"Coffee 100g", "Beverages", "8901234567896", "60.00", "90.00", "18.00", "1007", "25", "5"},
                {"Soap", "Personal Care", "8901234567897", "20.00", "35.00", "18.00", "1008", "60", "15"},
                {"Shampoo", "Personal Care", "8901234567898", "100.00", "150.00", "18.00", "1009", "20", "5"},
                {"Toothpaste", "Personal Care", "8901234567899", "45.00", "70.00", "18.00", "1010", "40", "10"},
            };

            for (String[] p : products) {
                Product product = Product.builder()
                        .name(p[0])
                        .category(p[1])
                        .barcode(p[2])
                        .qrCode(qrCodeGenerator.generateQRCode(p[2]))
                        .purchasePrice(new BigDecimal(p[3]))
                        .sellingPrice(new BigDecimal(p[4]))
                        .gstPercentage(new BigDecimal(p[5]))
                        .hsnCode(p[6])
                        .quantity(Integer.parseInt(p[7]))
                        .minimumQuantity(Integer.parseInt(p[8]))
                        .active(true)
                        .build();
                productRepository.save(product);
            }
            log.info("Sample products created");
        }
    }

    private void seedSettings() {
        if (settingsRepository.count() == 0) {
            Settings settings = Settings.builder()
                    .companyName("My Billing Store")
                    .companyAddress("123 Main Street, City")
                    .companyPhone("+91 98765 43210")
                    .companyEmail("store@example.com")
                    .gstNumber("27AABCU9603R1ZX")
                    .invoiceSize("A4")
                    .darkMode(false)
                    .build();
            settingsRepository.save(settings);
            log.info("Default settings created");
        }
    }
}