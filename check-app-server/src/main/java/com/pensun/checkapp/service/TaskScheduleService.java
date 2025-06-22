package com.pensun.checkapp.service;

import com.pensun.checkapp.dto.TaskScheduleDTO;
import com.pensun.checkapp.dto.TaskCreateDTO;
import com.pensun.checkapp.dto.TaskConflictCheckDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 任务排班服务接口
 */
public interface TaskScheduleService {
    
    /**
     * 获取任务列表（分页）
     *
     * @param page 页码
     * @param size 每页大小
     * @param inspectorId 巡检员ID
     * @param areaId 区域ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 任务列表和分页信息
     */
    Map<String, Object> getTasks(Integer page, Integer size, Long inspectorId, 
                                Long areaId, String startDate, String endDate);
    
    /**
     * 创建新任务
     *
     * @param taskDTO 任务信息
     */
    void createTask(TaskCreateDTO taskDTO);
    
    /**
     * 更新任务
     *
     * @param taskId 任务ID
     * @param taskDTO 任务信息
     */
    void updateTask(Long taskId, TaskCreateDTO taskDTO);
    
    /**
     * 删除任务
     *
     * @param taskId 任务ID
     */
    void deleteTask(Long taskId);
    
    /**
     * 获取任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    TaskScheduleDTO getTaskDetail(Long taskId);
    
    /**
     * 检查排班冲突
     *
     * @param inspectorId 巡检员ID
     * @param scheduledTime 计划时间
     * @param excludeTaskId 排除的任务ID（编辑时使用）
     * @return 冲突检查结果
     */
    TaskConflictCheckDTO checkScheduleConflict(Long inspectorId, LocalDateTime scheduledTime, 
                                              Long excludeTaskId);
    
    /**
     * 获取指定日期范围内的任务
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 任务列表
     */
    List<TaskScheduleDTO> getTasksByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * 批量创建任务
     *
     * @param batchTasks 批量任务列表
     * @return 创建结果统计
     */
    Map<String, Object> batchCreateTasks(List<TaskCreateDTO> batchTasks);
    
    /**
     * 获取巡检员工作统计
     *
     * @param inspectorId 巡检员ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 工作统计数据
     */
    Map<String, Object> getInspectorStats(Long inspectorId, String startDate, String endDate);
} 