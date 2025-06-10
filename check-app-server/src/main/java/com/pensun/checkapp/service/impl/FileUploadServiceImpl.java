package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.FileUploadDTO;
import com.pensun.checkapp.entity.FileUpload;
import com.pensun.checkapp.mapper.FileUploadMapper;
import com.pensun.checkapp.service.FileUploadService;
import com.pensun.checkapp.service.SystemParamService;
import com.pensun.checkapp.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 文件上传Service实现类
 */
@Slf4j
@Service
public class FileUploadServiceImpl extends ServiceImpl<FileUploadMapper, FileUpload> implements FileUploadService {

    @Value("${file.upload.path:/data/uploads}")
    private String uploadPath;
    
    @Autowired
    private SystemParamService systemParamService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<FileUploadDTO> upload(MultipartFile file, String businessType, Long businessId) {
        try {
            if (file.isEmpty()) {
                return Result.failed("上传文件不能为空");
            }
            
            // 获取配置的文件类型和大小限制
            String allowedTypes = systemParamService.getParamValue("file.allowed_types", "jpg,jpeg,png,pdf,doc,docx");
            long maxSize = Long.parseLong(systemParamService.getParamValue("file.max_size", "10485760")); // 默认10MB
            
            // 检查文件类型
            String originalFilename = file.getOriginalFilename();
            String fileType = getFileExtension(originalFilename);
            if (!isAllowedFileType(fileType, allowedTypes)) {
                return Result.failed("不支持的文件类型");
            }
            
            // 检查文件大小
            if (file.getSize() > maxSize) {
                return Result.failed("文件大小超过限制");
            }
            
            // 获取当前用户ID
            Long currentUserId = SecurityUtils.getCurrentUserId();
            
            // 生成文件保存路径
            String saveDir = generateSavePath();
            String fileName = UUID.randomUUID().toString() + "." + fileType;
            String filePath = saveDir + "/" + fileName;
            
            // 创建目录
            File dir = new File(uploadPath + "/" + saveDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // 保存文件
            file.transferTo(new File(uploadPath + "/" + filePath));
            
            // 保存文件信息到数据库
            FileUpload fileUpload = new FileUpload();
            fileUpload.setFileName(originalFilename);
            fileUpload.setFilePath(filePath);
            fileUpload.setFileSize(file.getSize());
            fileUpload.setFileType(file.getContentType());
            fileUpload.setUploadUserId(currentUserId);
            fileUpload.setBusinessType(businessType);
            fileUpload.setBusinessId(businessId);
            fileUpload.setCreateTime(LocalDateTime.now());
            save(fileUpload);
            
            // 转换为DTO返回
            FileUploadDTO dto = new FileUploadDTO();
            BeanUtils.copyProperties(fileUpload, dto);
            
            return Result.success(dto);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.failed("文件上传失败: " + e.getMessage());
        }
    }
    
    @Override
    public FileUploadDTO getFileInfo(Long id) {
        FileUpload fileUpload = getById(id);
        if (fileUpload != null) {
            FileUploadDTO dto = new FileUploadDTO();
            BeanUtils.copyProperties(fileUpload, dto);
            return dto;
        }
        return null;
    }
    
    @Override
    public Result<List<FileUploadDTO>> getFilesByBusiness(String businessType, Long businessId) {
        try {
            if (!StringUtils.hasText(businessType)) {
                return Result.failed("业务类型不能为空");
            }
            
            LambdaQueryWrapper<FileUpload> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FileUpload::getBusinessType, businessType);
            
            if (businessId != null) {
                wrapper.eq(FileUpload::getBusinessId, businessId);
            }
            
            wrapper.orderByDesc(FileUpload::getCreateTime);
            
            List<FileUpload> fileUploads = list(wrapper);
            List<FileUploadDTO> dtoList = fileUploads.stream().map(file -> {
                FileUploadDTO dto = new FileUploadDTO();
                BeanUtils.copyProperties(file, dto);
                return dto;
            }).collect(Collectors.toList());
            
            return Result.success(dtoList);
        } catch (Exception e) {
            log.error("获取文件列表失败", e);
            return Result.failed("获取文件列表失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> deleteFile(Long id) {
        try {
            FileUpload fileUpload = getById(id);
            if (fileUpload == null) {
                return Result.failed("文件不存在");
            }
            
            // 删除物理文件
            File file = new File(uploadPath + "/" + fileUpload.getFilePath());
            if (file.exists()) {
                file.delete();
            }
            
            // 删除数据库记录
            removeById(id);
            
            return Result.success(true);
        } catch (Exception e) {
            log.error("删除文件失败", e);
            return Result.failed("删除文件失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成文件保存路径（按年月日组织）
     *
     * @return 保存路径
     */
    private String generateSavePath() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return now.format(formatter);
    }
    
    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) return "";
        return filename.substring(dotIndex + 1).toLowerCase();
    }
    
    /**
     * 检查是否为允许的文件类型
     *
     * @param fileType 文件类型
     * @param allowedTypes 允许的类型列表（逗号分隔）
     * @return 是否允许
     */
    private boolean isAllowedFileType(String fileType, String allowedTypes) {
        if (!StringUtils.hasText(fileType)) return false;
        if (!StringUtils.hasText(allowedTypes)) return true;
        
        String[] types = allowedTypes.split(",");
        for (String type : types) {
            if (fileType.equalsIgnoreCase(type.trim())) {
                return true;
            }
        }
        return false;
    }
} 