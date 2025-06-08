package com.pensun.checkapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 问题处理请求DTO
 */
@Data
public class IssueProcessRequestDTO {
    
    /**
     * 问题ID
     */
    @NotNull(message = "问题ID不能为空")
    private Long issueId;
    
    /**
     * 处理内容
     */
    @NotBlank(message = "处理内容不能为空")
    private String content;
    
    /**
     * 处理结果：process-处理中，close-关闭问题
     */
    @NotBlank(message = "处理结果不能为空")
    private String action;
    
    /**
     * 图片（JSON数组存储多张图片URL）
     */
    private String images;
} 