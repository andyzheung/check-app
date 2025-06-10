package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.FileUploadDTO;
import com.pensun.checkapp.entity.IssueProcess;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 问题处理Service接口
 */
public interface IssueProcessService extends IService<IssueProcess> {
    
    /**
     * 上传问题处理图片
     *
     * @param processId 问题处理ID
     * @param files 图片文件列表
     * @return 上传结果
     */
    Result<List<FileUploadDTO>> uploadImages(Long processId, List<MultipartFile> files);
    
    /**
     * 获取问题处理图片
     *
     * @param processId 问题处理ID
     * @return 图片列表
     */
    Result<List<FileUploadDTO>> getImages(Long processId);
    
    /**
     * 删除问题处理图片
     *
     * @param processId 问题处理ID
     * @param imageId 图片ID
     * @return 删除结果
     */
    Result<Boolean> deleteImage(Long processId, Long imageId);
} 