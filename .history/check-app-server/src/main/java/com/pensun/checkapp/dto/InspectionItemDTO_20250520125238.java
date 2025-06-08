package com.pensun.checkapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 巡检项DTO
 */
@Data
public class InspectionItemDTO {
    
    /**
     * 巡检项ID
     */
    private Long id;
    
    /**
     * 巡检记录ID
     */
    private Long recordId;
    
    /**
     * 巡检项名称
     */
    private String name;
    
    /**
     * 巡检项类型：environment-环境巡检项，security-安全巡检项
     */
    private String type;
    
    /**
     * 巡检项状态：normal-正常，abnormal-异常
     */
    private String status;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 