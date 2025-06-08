package com.pensun.checkapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 巡检记录查询DTO
 */
@Data
public class InspectionRecordQueryDTO {
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 巡检区域ID
     */
    private Long areaId;
    
    /**
     * 巡检人员ID
     */
    private Long inspectorId;
    
    /**
     * 巡检状态：normal-正常，abnormal-异常
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