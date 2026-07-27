package com.billing.service;

import com.billing.dto.ProductDTO;
import com.billing.dto.ProductRequest;
import com.billing.entity.Product;
import com.billing.repository.ProductRepository;
import com.billing.util.BarcodeGenerator;
import com.billing.util.QRCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final BarcodeGenerator barcodeGenerator;
    private final QRCodeGenerator qrCodeGenerator;

    public ProductService(ProductRepository productRepository, BarcodeGenerator barcodeGenerator, QRCodeGenerator qrCodeGenerator) {
        this.productRepository = productRepository;
        this.barcodeGenerator = barcodeGenerator;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDTO(product);
    }

    public ProductDTO getProductByBarcode(String barcode) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDTO(product);
    }

    @Transactional
    public ProductDTO createProduct(ProductRequest request) {
        String barcode = request.getBarcode();
        if (barcode == null || barcode.isEmpty()) {
            barcode = barcodeGenerator.generateBarcode();
        }
        String qrCode = qrCodeGenerator.generateQRCode(barcode);

        Product product = Product.builder()
                .name(request.getName())
                .category(request.getCategory())
                .barcode(barcode)
                .qrCode(qrCode)
                .imageUrl(request.getImageUrl())
                .purchasePrice(request.getPurchasePrice())
                .sellingPrice(request.getSellingPrice())
                .gstPercentage(request.getGstPercentage())
                .hsnCode(request.getHsnCode())
                .quantity(request.getQuantity())
                .minimumQuantity(request.getMinimumQuantity())
                .active(true)
                .build();

        productRepository.save(product);
        return mapToDTO(product);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setGstPercentage(request.getGstPercentage());
        product.setHsnCode(request.getHsnCode());
        product.setQuantity(request.getQuantity());
        product.setMinimumQuantity(request.getMinimumQuantity());
        if (request.getImageUrl() != null) product.setImageUrl(request.getImageUrl());

        if (request.getBarcode() != null && !request.getBarcode().isBlank() && !request.getBarcode().equals(product.getBarcode())) {
            product.setBarcode(request.getBarcode());
            product.setQrCode(qrCodeGenerator.generateQRCode(request.getBarcode()));
        } else if (product.getQrCode() == null || product.getQrCode().isEmpty()) {
            String codeToEncode = (product.getBarcode() != null && !product.getBarcode().isEmpty()) ? product.getBarcode() : barcodeGenerator.generateBarcode();
            product.setBarcode(codeToEncode);
            product.setQrCode(qrCodeGenerator.generateQRCode(codeToEncode));
        }

        productRepository.save(product);
        return mapToDTO(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }

    public List<ProductDTO> searchProducts(String search) {
        return productRepository.searchProducts(search).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public String getOrGenerateProductQRCode(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        if (isValidQrCode(product.getQrCode())) {
            return product.getQrCode();
        }

        String codeToEncode = (product.getBarcode() != null && !product.getBarcode().isEmpty())
                ? product.getBarcode()
                : (product.getName() != null ? product.getName() : "PROD-" + product.getId());

        String qrCode = qrCodeGenerator.generateQRCode(codeToEncode);
        product.setQrCode(qrCode);
        productRepository.save(product);
        return qrCode;
    }

    @Transactional
    public int generateAllMissingQRCodes() {
        List<Product> products = productRepository.findByActiveTrue();
        int count = 0;
        for (Product product : products) {
            if (!isValidQrCode(product.getQrCode())) {
                String codeToEncode = (product.getBarcode() != null && !product.getBarcode().isEmpty())
                        ? product.getBarcode()
                        : (product.getName() != null ? product.getName() : "PROD-" + product.getId());
                product.setQrCode(qrCodeGenerator.generateQRCode(codeToEncode));
                productRepository.save(product);
                count++;
            }
        }
        return count;
    }

    private boolean isValidQrCode(String qrCode) {
        return qrCode != null && (qrCode.startsWith("data:image/") || qrCode.startsWith("http://") || qrCode.startsWith("https://"));
    }

    private ProductDTO mapToDTO(Product product) {
        String qrCode = product.getQrCode();
        if (!isValidQrCode(qrCode)) {
            String codeToEncode = (product.getBarcode() != null && !product.getBarcode().isEmpty())
                    ? product.getBarcode()
                    : (product.getName() != null ? product.getName() : "PROD-" + product.getId());
            qrCode = qrCodeGenerator.generateQRCode(codeToEncode);
        }

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .barcode(product.getBarcode())
                .qrCode(qrCode)
                .imageUrl(product.getImageUrl())
                .purchasePrice(product.getPurchasePrice())
                .sellingPrice(product.getSellingPrice())
                .gstPercentage(product.getGstPercentage())
                .hsnCode(product.getHsnCode())
                .quantity(product.getQuantity())
                .minimumQuantity(product.getMinimumQuantity())
                .active(product.isActive())
                .lowStock(product.getQuantity() <= product.getMinimumQuantity())
                .build();
    }
}