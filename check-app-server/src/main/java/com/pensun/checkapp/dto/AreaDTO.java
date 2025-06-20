package com.pensun.checkapp.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.pensun.checkapp.entity.Area;
import com.pensun.checkapp.entity.InspectionItemTemplate;
import lombok.EqualsAndHashCode;

/**
 * 区域DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AreaDTO extends Area {
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
    
    private List<InspectionItemTemplate> inspectionItems;
    
    @Data
    public static class TaskDTO {
        private Long taskId;
        private String taskName;
        private Integer priority;
    }
} 