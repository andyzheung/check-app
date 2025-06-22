package com.pensun.checkapp.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.pensun.checkapp.entity.InspectionItemTemplate;
import com.fasterxml.jackson.annotation.JsonProperty;

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
     * 地址
     */
    private String address;
    
    /**
     * 模块数量（仅数据中心使用）
     */
    private Integer moduleCount;
    
    /**
     * 区域配置（JSON格式）
     */
    private String configJson;
    
    /**
     * 模块列表（解析后的配置）
     */
    private List<ModuleDTO> modules;
    
    /**
     * 关联的巡检任务
     */
    private List<TaskDTO> tasks;
    
    /**
     * 巡检项目模板
     */
    private List<InspectionItemTemplate> inspectionItems;
    
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
    
    @Data
    public static class ModuleDTO {
        private Long id;
        private String name;
        private String type;
    }
} 