package com.pensun.checkapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 问题处理记录DTO
 */
@Data
public class IssueProcessDTO {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * 问题ID
     */
    private Long issueId;
    
    /**
     * 操作类型：create-创建，process-处理，close-关闭
     */
    private String action;
    
    /**
     * 处理人ID
     */
    private Long processorId;
    
    /**
     * 处理人姓名
     */
    private String processorName;
    
    /**
     * 处理时间
     */
    private LocalDateTime processTime;
    
    /**
     * 处理内容
     */
    private String content;
    
    /**
     * 图片（JSON数组存储多张图片URL）
     */
    private String images;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 