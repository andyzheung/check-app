package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.FileUploadDTO;
import com.pensun.checkapp.entity.IssueProcess;
import com.pensun.checkapp.mapper.IssueProcessMapper;
import com.pensun.checkapp.service.FileUploadService;
import com.pensun.checkapp.service.IssueProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题处理Service实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IssueProcessServiceImpl extends ServiceImpl<IssueProcessMapper, IssueProcess> implements IssueProcessService {

    private final FileUploadService fileUploadService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<List<FileUploadDTO>> uploadImages(Long processId, List<MultipartFile> files) {
        if (processId == null) {
            return Result.failed("问题处理ID不能为空");
        }
        
        if (files == null || files.isEmpty()) {
            return Result.failed("上传文件不能为空");
        }
        
        try {
            // 查询问题处理记录
            IssueProcess process = getById(processId);
            if (process == null) {
                return Result.failed("问题处理记录不存在");
            }
            
            // 上传图片
            List<FileUploadDTO> uploadResults = new ArrayList<>();
            for (MultipartFile file : files) {
                Result<FileUploadDTO> uploadResult = fileUploadService.upload(file, "issue_process", processId);
                if (uploadResult.getCode() == 0 && uploadResult.getData() != null) {
                    uploadResults.add(uploadResult.getData());
                }
            }
            
            // 更新问题处理记录的图片字段
            if (!uploadResults.isEmpty()) {
                // 获取现有图片列表
                String existingImages = process.getImages();
                List<String> imageUrls = new ArrayList<>();
                
                // 解析现有图片URL
                if (existingImages != null && !existingImages.isEmpty()) {
                    try {
                        imageUrls = com.alibaba.fastjson.JSON.parseArray(existingImages, String.class);
                    } catch (Exception e) {
                        log.warn("解析图片URL失败: {}", e.getMessage());
                    }
                }
                
                // 添加新上传的图片URL
                for (FileUploadDTO uploadResult : uploadResults) {
                    imageUrls.add(uploadResult.getFilePath());
                }
                
                // 更新问题处理记录
                process.setImages(com.alibaba.fastjson.JSON.toJSONString(imageUrls));
                updateById(process);
            }
            
            return Result.success(uploadResults);
        } catch (Exception e) {
            log.error("上传问题处理图片失败", e);
            throw new RuntimeException("上传问题处理图片失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<FileUploadDTO>> getImages(Long processId) {
        if (processId == null) {
            return Result.failed("问题处理ID不能为空");
        }
        
        try {
            // 查询问题处理记录
            IssueProcess process = getById(processId);
            if (process == null) {
                return Result.failed("问题处理记录不存在");
            }
            
            // 获取图片列表
            String images = process.getImages();
            if (images == null || images.isEmpty()) {
                return Result.success(new ArrayList<>());
            }
            
            // 解析图片URL
            List<String> imageUrls;
            try {
                imageUrls = com.alibaba.fastjson.JSON.parseArray(images, String.class);
            } catch (Exception e) {
                log.warn("解析图片URL失败: {}", e.getMessage());
                return Result.success(new ArrayList<>());
            }
            
            // 构建结果
            List<FileUploadDTO> result = new ArrayList<>();
            for (String imageUrl : imageUrls) {
                FileUploadDTO fileUploadDTO = new FileUploadDTO();
                fileUploadDTO.setFilePath(imageUrl);
                fileUploadDTO.setFileName(imageUrl.substring(imageUrl.lastIndexOf("/") + 1));
                result.add(fileUploadDTO);
            }
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取问题处理图片失败", e);
            throw new RuntimeException("获取问题处理图片失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> deleteImage(Long processId, Long imageId) {
        if (processId == null) {
            return Result.failed("问题处理ID不能为空");
        }
        
        if (imageId == null) {
            return Result.failed("图片ID不能为空");
        }
        
        try {
            // 查询问题处理记录
            IssueProcess process = getById(processId);
            if (process == null) {
                return Result.failed("问题处理记录不存在");
            }
            
            // 删除文件
            Result<Boolean> deleteResult = fileUploadService.deleteFile(imageId);
            if (deleteResult.getCode() != 0) {
                return deleteResult;
            }
            
            // 更新问题处理记录的图片字段
            String images = process.getImages();
            if (images != null && !images.isEmpty()) {
                try {
                    List<String> imageUrls = com.alibaba.fastjson.JSON.parseArray(images, String.class);
                    
                    // 获取要删除的图片URL
                    FileUploadDTO fileUploadDTO = fileUploadService.getFileInfo(imageId);
                    if (fileUploadDTO != null) {
                        imageUrls.remove(fileUploadDTO.getFilePath());
                        
                        // 更新问题处理记录
                        process.setImages(com.alibaba.fastjson.JSON.toJSONString(imageUrls));
                        updateById(process);
                    }
                } catch (Exception e) {
                    log.warn("解析图片URL失败: {}", e.getMessage());
                }
            }
            
            return Result.success(true);
        } catch (Exception e) {
            log.error("删除问题处理图片失败", e);
            throw new RuntimeException("删除问题处理图片失败: " + e.getMessage());
        }
    }
} 