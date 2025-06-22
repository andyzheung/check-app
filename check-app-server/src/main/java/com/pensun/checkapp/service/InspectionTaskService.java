package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.entity.InspectionTask;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 巡检任务服务接口
 */
public interface InspectionTaskService extends IService<InspectionTask> {
    
    /**
     * 根据日期范围获取任务
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 任务列表
     */
    List<InspectionTask> getTasksByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 根据巡检员ID获取任务
     * @param inspectorId 巡检员ID
     * @return 任务列表
     */
    List<InspectionTask> getTasksByInspector(Long inspectorId);
    
    /**
     * 根据区域ID获取任务
     * @param areaId 区域ID
     * @return 任务列表
     */
    List<InspectionTask> getTasksByArea(Long areaId);
} 