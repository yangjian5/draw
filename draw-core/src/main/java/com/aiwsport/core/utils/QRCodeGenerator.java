package com.aiwsport.core.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    private static final String QR_CODE_IMAGE_PATH = "/home/www-data/data1/qrcode";

//    private static final String QR_CODE_IMAGE_PATH = "/Users/yangjian9/Desktop/";

    public static void generateQRCodeImage(String text, String name) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 350, 350);
            Path path = FileSystems.getDefault().getPath(QR_CODE_IMAGE_PATH+name+".png");

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        generateQRCodeImage("This is my first QR Code", "123");
    }


}