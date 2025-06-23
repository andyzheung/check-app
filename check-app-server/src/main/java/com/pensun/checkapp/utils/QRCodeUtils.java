package com.pensun.checkapp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        String areaCode = (String) data.get("areaCode");
        log.info("开始生成二维码 - 区域编码: {}, 输出目录: {}", areaCode, outputDir);
        
        try {
            // 验证输入参数
            if (data == null || !data.containsKey("areaCode")) {
                throw new IllegalArgumentException("区域编码不能为空");
            }
            
            // 创建输出目录
            Path outputPath = Paths.get(outputDir);
            if (!Files.exists(outputPath)) {
                log.info("创建二维码输出目录: {}", outputPath.toAbsolutePath());
                Files.createDirectories(outputPath);
            }
            
            // 检查目录权限
            if (!Files.isWritable(outputPath)) {
                throw new RuntimeException("二维码输出目录没有写入权限: " + outputPath.toAbsolutePath());
            }
            
            // 创建二维码生成器
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            
            // 设置二维码参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 4);
            
            // 生成二维码矩阵
            String content = objectMapper.writeValueAsString(data);
            log.debug("二维码内容: {}", content);
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
            
            // 生成二维码图片
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            
            // 保存二维码图片
            String fileName = areaCode + "_" + System.currentTimeMillis() + ".png";
            Path filePath = outputPath.resolve(fileName);
            log.info("保存二维码文件: {}", filePath.toAbsolutePath());
            
            ImageIO.write(qrImage, "PNG", filePath.toFile());
            
            // 验证文件是否成功创建
            if (!Files.exists(filePath)) {
                throw new RuntimeException("二维码文件创建失败: " + filePath.toAbsolutePath());
            }
            
            long fileSize = Files.size(filePath);
            log.info("二维码生成成功 - 文件: {}, 大小: {} bytes", fileName, fileSize);
            
            return "/qrcode/" + fileName;
        } catch (Exception e) {
            log.error("生成二维码失败 - 区域编码: {}, 错误信息: {}", areaCode, e.getMessage(), e);
            throw new RuntimeException("生成二维码失败: " + e.getMessage(), e);
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