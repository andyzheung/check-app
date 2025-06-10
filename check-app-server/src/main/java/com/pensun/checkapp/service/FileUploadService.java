package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.FileUploadDTO;
import com.pensun.checkapp.entity.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传Service接口
 */
public interface FileUploadService extends IService<FileUpload> {
    
    /**
     * 上传文件
     *
     * @param file 文件
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 上传结果
     */
    Result<FileUploadDTO> upload(MultipartFile file, String businessType, Long businessId);
    
    /**
     * 获取文件信息
     *
     * @param id 文件ID
     * @return 文件信息
     */
    FileUploadDTO getFileInfo(Long id);
    
    /**
     * 根据业务类型和业务ID获取文件列表
     *
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 文件列表
     */
    Result<java.util.List<FileUploadDTO>> getFilesByBusiness(String businessType, Long businessId);
    
    /**
     * 删除文件
     *
     * @param id 文件ID
     * @return 删除结果
     */
    Result<Boolean> deleteFile(Long id);
} 