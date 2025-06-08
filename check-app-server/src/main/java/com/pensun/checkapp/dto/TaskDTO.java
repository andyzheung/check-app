package com.pensun.checkapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private Long id;

    @NotBlank(message = "任务名称不能为空")
    private String name;

    @NotBlank(message = "任务描述不能为空")
    private String description;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @NotNull(message = "巡检点不能为空")
    private Long pointId;

    private String status;
    private String remark;
} 