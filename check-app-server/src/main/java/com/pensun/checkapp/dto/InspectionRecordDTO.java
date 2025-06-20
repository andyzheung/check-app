package com.pensun.checkapp.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 巡检记录DTO
 */
@Data
public class InspectionRecordDTO {
    
    /**
     * 记录ID
     */
    private Long id;
    
    /**
     * 记录编号
     */
    private String recordNo;
    
    /**
     * 巡检任务ID
     */
    private Long taskId;
    
    /**
     * 巡检人员ID
     */
    private Long inspectorId;
    
    /**
     * 巡检人员姓名
     */
    private String inspectorName;
    
    /**
     * 巡检区域ID
     */
    private Long areaId;
    
    /**
     * 巡检区域名称
     */
    private String areaName;
    
    /**
     * 巡检开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 巡检结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 巡检状态：normal-正常，abnormal-异常
     */
    private String status;
    
    /**
     * 巡检备注
     */
    private String remark;
    
    /**
     * 巡检路径（JSON格式存储）
     */
    private String routePath;
    
    /**
     * 环境巡检项列表
     */
    private List<InspectionItemDTO> environmentItems;
    
    /**
     * 安全巡检项列表
     */
    private List<InspectionItemDTO> securityItems;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 巡检日期
     */
    private String inspectionDate;
    
    /**
     * 巡检项总数
     */
    private Integer totalItems;
    
    /**
     * 正常巡检项数
     */
    private Integer normalItems;
    
    /**
     * 异常巡检项数
     */
    private Integer abnormalItems;
    
    /**
     * 巡检记录详情列表
     */
    private List<InspectionRecordDetailDTO> details;
} 