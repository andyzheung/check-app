package com.pensun.checkapp.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务排班DTO
 */
@Data
public class TaskScheduleDTO {
    
    /**
     * 任务ID
     */
    private Long id;
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 区域ID
     */
    private Long areaId;
    
    /**
     * 区域名称
     */
    private String areaName;
    
    /**
     * 区域编码
     */
    private String areaCode;
    
    /**
     * 巡检员ID
     */
    private Long inspectorId;
    
    /**
     * 巡检员姓名
     */
    private String inspectorName;
    
    /**
     * 计划执行时间
     */
    private LocalDateTime scheduledTime;
    
    /**
     * 任务优先级：low-低，normal-普通，high-高，urgent-紧急
     */
    private String priority;
    
    /**
     * 任务状态：pending-待执行，in_progress-进行中，completed-已完成，cancelled-已取消
     */
    private String status;
    
    /**
     * 任务描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 实际开始时间
     */
    private LocalDateTime actualStartTime;
    
    /**
     * 实际完成时间
     */
    private LocalDateTime actualEndTime;
    
    /**
     * 备注
     */
    private String remarks;
} 