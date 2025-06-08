package com.pensun.checkapp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类
 */
@Component
@RequiredArgsConstructor
public class QRCodeUtils {
    
    private final ObjectMapper objectMapper;
    
    @Value("${qrcode.secret-key}")
    private String secretKey;
    
    @Value("${qrcode.output-dir}")
    private String outputDir;
    
    /**
     * 生成二维码
     *
     * @param data 二维码数据
     * @return 二维码URL
     */
    public String generateQRCode(Map<String, Object> data) {
        try {
            // 创建二维码生成器
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            
            // 设置二维码参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 4);
            
            // 生成二维码矩阵
            String content = objectMapper.writeValueAsString(data);
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
            
            // 生成二维码图片
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            
            // 保存二维码图片
            String fileName = data.get("areaCode") + "_" + System.currentTimeMillis() + ".png";
            Path filePath = Paths.get(outputDir, fileName);
            Files.createDirectories(filePath.getParent());
            ImageIO.write(qrImage, "PNG", filePath.toFile());
            
            return "/qrcode/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("生成二维码失败", e);
        }
    }
    
    /**
     * 生成签名
     *
     * @param data 数据
     * @return 签名
     */
    public String generateSignature(Map<String, Object> data) {
        try {
            String content = data.get("areaCode") + ":" + data.get("timestamp") + ":" + secretKey;
            return DigestUtils.md5DigestAsHex(content.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("生成签名失败", e);
        }
    }
    
    /**
     * 验证二维码
     *
     * @param qrData 二维码数据
     * @return 验证结果
     */
    public Boolean verifyQRCode(String qrData) {
        try {
            // 解析二维码数据
            Map<String, Object> data = objectMapper.readValue(qrData, Map.class);
            
            // 验证签名
            String signature = (String) data.remove("signature");
            String expectedSignature = generateSignature(data);
            
            return signature.equals(expectedSignature);
        } catch (Exception e) {
            return false;
        }
    }
} 