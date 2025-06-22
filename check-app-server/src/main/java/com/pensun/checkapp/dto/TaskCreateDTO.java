package com.pensun.checkapp.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 任务创建/编辑DTO
 */
@Data
public class TaskCreateDTO {
    
    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    private String taskName;
    
    /**
     * 区域ID
     */
    @NotNull(message = "区域ID不能为空")
    private Long areaId;
    
    /**
     * 巡检员ID
     */
    @NotNull(message = "巡检员ID不能为空")
    private Long inspectorId;
    
    /**
     * 计划执行时间
     */
    @NotNull(message = "计划时间不能为空")
    private LocalDateTime scheduledTime;
    
    /**
     * 任务优先级：low-低，normal-普通，high-高，urgent-紧急
     */
    private String priority = "normal";
    
    /**
     * 任务描述
     */
    private String description;
    
    /**
     * 任务状态：pending-待执行，in_progress-进行中，completed-已完成，cancelled-已取消
     */
    private String status = "pending";
} 