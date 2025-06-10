package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.FileUploadDTO;
import com.pensun.checkapp.service.IssueProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 问题处理Controller
 * 
 * [COMMON] - 该接口可供移动应用和管理后台共用
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/issue-processes")
public class IssueProcessController {

    @Autowired
    private IssueProcessService issueProcessService;

    /**
     * 上传问题处理图片
     * [COMMON]
     *
     * @param id 问题处理ID
     * @param files 图片文件列表
     * @return 上传结果
     */
    @PostMapping("/{id}/images")
    public Result<List<FileUploadDTO>> uploadImages(
            @PathVariable Long id, 
            @RequestParam("files") List<MultipartFile> files) {
        log.info("上传问题处理图片: processId={}, fileCount={}", id, files.size());
        return issueProcessService.uploadImages(id, files);
    }

    /**
     * 获取问题处理图片
     * [COMMON]
     *
     * @param id 问题处理ID
     * @return 图片列表
     */
    @GetMapping("/{id}/images")
    public Result<List<FileUploadDTO>> getImages(@PathVariable Long id) {
        log.info("获取问题处理图片: processId={}", id);
        return issueProcessService.getImages(id);
    }

    /**
     * 删除问题处理图片
     * [COMMON]
     *
     * @param id 问题处理ID
     * @param imageId 图片ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}/images/{imageId}")
    public Result<Boolean> deleteImage(@PathVariable Long id, @PathVariable Long imageId) {
        log.info("删除问题处理图片: processId={}, imageId={}", id, imageId);
        return issueProcessService.deleteImage(id, imageId);
    }
} 