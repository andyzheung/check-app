package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.FileUploadDTO;
import com.pensun.checkapp.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传Controller
 * 
 * [COMMON] - 该接口可供移动应用和管理后台共用
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/files")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 上传文件
     * [COMMON]
     *
     * @param file 文件
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 上传结果
     */
    @PostMapping("/upload")
    public Result<FileUploadDTO> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "businessType", required = false) String businessType,
            @RequestParam(value = "businessId", required = false) Long businessId) {
        log.info("上传文件: filename={}, businessType={}, businessId={}", 
                file.getOriginalFilename(), businessType, businessId);
        return fileUploadService.upload(file, businessType, businessId);
    }

    /**
     * 获取文件列表
     * [COMMON]
     *
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 文件列表
     */
    @GetMapping("/list")
    public Result<List<FileUploadDTO>> list(
            @RequestParam("businessType") String businessType,
            @RequestParam("businessId") Long businessId) {
        log.info("获取文件列表: businessType={}, businessId={}", businessType, businessId);
        return fileUploadService.getFilesByBusiness(businessType, businessId);
    }

    /**
     * 删除文件
     * [COMMON]
     *
     * @param id 文件ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        log.info("删除文件: id={}", id);
        return fileUploadService.deleteFile(id);
    }
    
    /**
     * 获取文件信息
     * [COMMON]
     *
     * @param id 文件ID
     * @return 文件信息
     */
    @GetMapping("/{id}")
    public Result<FileUploadDTO> getFile(@PathVariable Long id) {
        log.info("获取文件信息: id={}", id);
        FileUploadDTO fileInfo = fileUploadService.getFileInfo(id);
        return Result.success(fileInfo);
    }
} 