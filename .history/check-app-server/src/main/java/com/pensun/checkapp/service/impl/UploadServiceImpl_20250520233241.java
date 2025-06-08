package com.pensun.checkapp.service.impl;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public Result upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            return Result.success(filename);
        } catch (IOException e) {
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }

    @Override
    public void download(String filename) {
        try {
            Path filePath = Paths.get(uploadPath, filename);
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException("文件不存在");
            }

            HttpServletResponse response = ((HttpServletResponse) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes().getResponse());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);

            Files.copy(filePath, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败：" + e.getMessage());
        }
    }
} 