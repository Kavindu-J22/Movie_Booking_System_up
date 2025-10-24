package com.moviebooking.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class QRCodeService {

    public String generateQRCodeBase64(String data) throws WriterException, IOException {
        return generateQRCodeBase64(data, 300, 300);
    }

    public String generateQRCodeBase64(String data, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        byte[] qrCodeBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(qrCodeBytes);
    }

    public boolean validateQRCodeData(String qrCodeData) {
        // Basic validation for QR code data format
        if (qrCodeData == null || qrCodeData.isEmpty()) {
            return false;
        }
        
        // Check if it follows our format: MOVIE_TICKET:bookingId:timestamp
        String[] parts = qrCodeData.split(":");
        return parts.length == 3 && "MOVIE_TICKET".equals(parts[0]);
    }

    public String extractBookingIdFromQRCode(String qrCodeData) {
        if (!validateQRCodeData(qrCodeData)) {
            return null;
        }
        
        String[] parts = qrCodeData.split(":");
        return parts[1]; // booking ID is the second part
    }
}
