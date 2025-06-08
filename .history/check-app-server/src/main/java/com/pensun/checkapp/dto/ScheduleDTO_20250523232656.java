package com.pensun.checkapp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ScheduleDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long areaId;
    private String areaName;
    private LocalDate scheduleDate;
    private String shift;
    private String status;
    private String remark;
} 