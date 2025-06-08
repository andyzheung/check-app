package com.pensun.checkapp.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 区域DTO
 */
@Data
public class AreaDTO {
    /**
     * 区域ID
     */
    private Long id;
    
    /**
     * 区域编码
     */
    private String areaCode;
    
    /**
     * 区域名称
     */
    private String areaName;
    
    /**
     * 区域类型
     */
    private String areaType;
    
    /**
     * 区域类型名称
     */
    private String areaTypeName;
    
    /**
     * 二维码URL
     */
    private String qrCodeUrl;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 关联的巡检任务
     */
    private List<TaskDTO> tasks;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    @Data
    public static class TaskDTO {
        private Long taskId;
        private String taskName;
        private Integer priority;
    }
} 