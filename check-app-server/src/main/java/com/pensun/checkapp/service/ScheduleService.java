package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.dto.ScheduleDTO;
import com.pensun.checkapp.entity.InspectionSchedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService extends IService<InspectionSchedule> {
    
    /**
     * 获取用户今日排班
     *
     * @param userId 用户ID
     * @return 排班列表
     */
    List<ScheduleDTO> getTodaySchedules(Long userId);
    
    /**
     * 获取用户今日待完成任务数
     *
     * @param userId 用户ID
     * @return 待完成任务数
     */
    int getTodayPendingTaskCount(Long userId);
    
    /**
     * 获取用户今日已完成任务数
     *
     * @param userId 用户ID
     * @return 已完成任务数
     */
    int getTodayCompletedTaskCount(Long userId);
} 