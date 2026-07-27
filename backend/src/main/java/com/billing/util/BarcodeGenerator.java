package com.billing.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

@Component
public class BarcodeGenerator {

    public String generateBarcode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("890");
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public String generateBarcodeImage(String barcode) {
        try {
            EAN13Writer writer = new EAN13Writer();
            BitMatrix matrix = writer.encode(barcode, BarcodeFormat.EAN_13, 300, 150);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate barcode", e);
        }
    }
}