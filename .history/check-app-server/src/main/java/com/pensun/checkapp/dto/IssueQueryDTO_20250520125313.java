package com.pensun.checkapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 问题查询DTO
 */
@Data
public class IssueQueryDTO {
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 问题类型：environment-环境问题，security-安全问题，device-设备问题，other-其他
     */
    private String type;
    
    /**
     * 记录人ID
     */
    private Long recorderId;
    
    /**
     * 问题状态：open-未处理，processing-处理中，closed-已关闭
     */
    private String status;
    
    /**
     * 页码
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小
     */
    private Integer pageSize = 10;
} 