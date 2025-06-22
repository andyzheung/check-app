package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.TaskScheduleDTO;
import com.pensun.checkapp.dto.TaskCreateDTO;
import com.pensun.checkapp.dto.TaskConflictCheckDTO;
import com.pensun.checkapp.service.TaskScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 任务排班管理Controller
 * 
 * [ADMIN] - 该接口仅供管理后台使用
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/schedule")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TaskScheduleController {

    @Autowired
    private TaskScheduleService taskScheduleService;

    /**
     * 获取任务列表
     * [ADMIN]
     *
     * @param page 页码
     * @param size 每页大小
     * @param inspectorId 巡检员ID
     * @param areaId 区域ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 任务列表
     */
    @GetMapping("/tasks")
    public Result<Map<String, Object>> getTasks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) Long inspectorId,
            @RequestParam(required = false) Long areaId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        log.info("获取任务列表 - page: {}, size: {}, inspectorId: {}, areaId: {}", 
                page, size, inspectorId, areaId);
        
        Map<String, Object> result = taskScheduleService.getTasks(
                page, size, inspectorId, areaId, startDate, endDate);
        
        return Result.success(result);
    }

    /**
     * 创建新任务
     * [ADMIN]
     *
     * @param taskDTO 任务信息
     * @return 创建结果
     */
    @PostMapping("/tasks")
    public Result<Void> createTask(@Valid @RequestBody TaskCreateDTO taskDTO) {
        log.info("创建新任务: {}", taskDTO.getTaskName());
        
        taskScheduleService.createTask(taskDTO);
        
        return Result.success();
    }

    /**
     * 更新任务
     * [ADMIN]
     *
     * @param taskId 任务ID
     * @param taskDTO 任务信息
     * @return 更新结果
     */
    @PutMapping("/tasks/{taskId}")
    public Result<Void> updateTask(@PathVariable Long taskId, 
                                  @Valid @RequestBody TaskCreateDTO taskDTO) {
        log.info("更新任务: {} - {}", taskId, taskDTO.getTaskName());
        
        taskScheduleService.updateTask(taskId, taskDTO);
        
        return Result.success();
    }

    /**
     * 删除任务
     * [ADMIN]
     *
     * @param taskId 任务ID
     * @return 删除结果
     */
    @DeleteMapping("/tasks/{taskId}")
    public Result<Void> deleteTask(@PathVariable Long taskId) {
        log.info("删除任务: {}", taskId);
        
        taskScheduleService.deleteTask(taskId);
        
        return Result.success();
    }

    /**
     * 获取任务详情
     * [ADMIN]
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    @GetMapping("/tasks/{taskId}")
    public Result<TaskScheduleDTO> getTaskDetail(@PathVariable Long taskId) {
        log.info("获取任务详情: {}", taskId);
        
        TaskScheduleDTO task = taskScheduleService.getTaskDetail(taskId);
        
        return Result.success(task);
    }

    /**
     * 检查排班冲突
     * [ADMIN]
     *
     * @param inspectorId 巡检员ID
     * @param scheduledTime 计划时间
     * @param excludeTaskId 排除的任务ID（编辑时使用）
     * @return 冲突检查结果
     */
    @GetMapping("/conflict-check")
    public Result<TaskConflictCheckDTO> checkScheduleConflict(
            @RequestParam Long inspectorId,
            @RequestParam String scheduledTime,
            @RequestParam(required = false) Long excludeTaskId) {
        
        log.info("检查排班冲突 - inspectorId: {}, scheduledTime: {}, excludeTaskId: {}", 
                inspectorId, scheduledTime, excludeTaskId);
        
        TaskConflictCheckDTO result = taskScheduleService.checkScheduleConflict(
                inspectorId, LocalDateTime.parse(scheduledTime.replace(" ", "T")), excludeTaskId);
        
        return Result.success(result);
    }

    /**
     * 获取日历视图数据
     * [ADMIN]
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日历数据
     */
    @GetMapping("/calendar")
    public Result<List<TaskScheduleDTO>> getCalendarData(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        log.info("获取日历数据 - startDate: {}, endDate: {}", startDate, endDate);
        
        List<TaskScheduleDTO> tasks = taskScheduleService.getTasksByDateRange(
                LocalDate.parse(startDate), LocalDate.parse(endDate));
        
        return Result.success(tasks);
    }

    /**
     * 批量分配任务
     * [ADMIN]
     *
     * @param batchTasks 批量任务列表
     * @return 分配结果
     */
    @PostMapping("/tasks/batch")
    public Result<Map<String, Object>> batchCreateTasks(@Valid @RequestBody List<TaskCreateDTO> batchTasks) {
        log.info("批量创建任务，数量: {}", batchTasks.size());
        
        Map<String, Object> result = taskScheduleService.batchCreateTasks(batchTasks);
        
        return Result.success(result);
    }

    /**
     * 获取巡检员工作统计
     * [ADMIN]
     *
     * @param inspectorId 巡检员ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 工作统计
     */
    @GetMapping("/inspector-stats")
    public Result<Map<String, Object>> getInspectorStats(
            @RequestParam Long inspectorId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        log.info("获取巡检员工作统计 - inspectorId: {}", inspectorId);
        
        Map<String, Object> stats = taskScheduleService.getInspectorStats(
                inspectorId, startDate, endDate);
        
        return Result.success(stats);
    }
} 