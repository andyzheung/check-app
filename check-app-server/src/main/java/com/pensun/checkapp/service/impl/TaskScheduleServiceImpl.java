package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.dto.TaskScheduleDTO;
import com.pensun.checkapp.dto.TaskCreateDTO;
import com.pensun.checkapp.dto.TaskConflictCheckDTO;
import com.pensun.checkapp.entity.InspectionTask;
import com.pensun.checkapp.entity.Area;
import com.pensun.checkapp.entity.User;
import com.pensun.checkapp.mapper.InspectionTaskMapper;
import com.pensun.checkapp.mapper.AreaMapper;
import com.pensun.checkapp.mapper.UserMapper;
import com.pensun.checkapp.service.TaskScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务排班服务实现类
 */
@Slf4j
@Service
public class TaskScheduleServiceImpl implements TaskScheduleService {

    @Autowired
    private InspectionTaskMapper inspectionTaskMapper;
    
    @Autowired
    private AreaMapper areaMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> getTasks(Integer page, Integer size, Long inspectorId, 
                                       Long areaId, String startDate, String endDate) {
        
        Page<InspectionTask> pageParam = new Page<>(page, size);
        QueryWrapper<InspectionTask> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件 - 使用实际的数据库字段名
        if (inspectorId != null) {
            queryWrapper.eq("inspector_id", inspectorId);
        }
        if (areaId != null) {
            queryWrapper.eq("area_id", areaId);
        }
        if (startDate != null) {
            queryWrapper.ge("plan_time", startDate);
        }
        if (endDate != null) {
            queryWrapper.le("plan_time", endDate);
        }
        
        // 按计划时间倒序排列
        queryWrapper.orderByDesc("plan_time");
        
        IPage<InspectionTask> taskPage = inspectionTaskMapper.selectPage(pageParam, queryWrapper);
        
        // 转换为DTO
        List<TaskScheduleDTO> taskDTOs = taskPage.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", taskDTOs);
        result.put("total", taskPage.getTotal());
        result.put("current", taskPage.getCurrent());
        result.put("size", taskPage.getSize());
        result.put("pages", taskPage.getPages());
        
        return result;
    }

    @Override
    @Transactional
    public void createTask(TaskCreateDTO taskDTO) {
        log.info("创建新任务: {}", taskDTO.getTaskName());
        
        // 检查冲突
        TaskConflictCheckDTO conflictCheck = checkScheduleConflict(
                taskDTO.getInspectorId(), taskDTO.getScheduledTime(), null);
        
        if (conflictCheck.isHasConflict()) {
            throw new RuntimeException("时间冲突：" + conflictCheck.getConflictMessage());
        }
        
        InspectionTask task = new InspectionTask();
        task.setName(taskDTO.getTaskName());
        task.setPointId(taskDTO.getAreaId()); // area_id字段在实体中映射为pointId
        task.setInspectorId(taskDTO.getInspectorId());
        task.setPlanTime(taskDTO.getScheduledTime()); // scheduled_time字段在实体中为planTime
        task.setDescription(taskDTO.getDescription());
        task.setStatus("pending"); // 设置默认状态为待执行
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        task.setCreateUserId(1L); // TODO: 从当前登录用户获取
        
        inspectionTaskMapper.insert(task);
        
        log.info("创建任务成功: {} - {}", task.getId(), task.getName());
    }



    @Override
    @Transactional
    public void updateTask(Long taskId, TaskCreateDTO taskDTO) {
        InspectionTask existingTask = inspectionTaskMapper.selectById(taskId);
        if (existingTask == null) {
            throw new RuntimeException("任务不存在: " + taskId);
        }
        
        // 检查冲突（排除当前任务）
        TaskConflictCheckDTO conflictCheck = checkScheduleConflict(
                taskDTO.getInspectorId(), taskDTO.getScheduledTime(), taskId);
        
        if (conflictCheck.isHasConflict()) {
            throw new RuntimeException("时间冲突：" + conflictCheck.getConflictMessage());
        }
        
        existingTask.setName(taskDTO.getTaskName());
        existingTask.setPointId(taskDTO.getAreaId());
        existingTask.setInspectorId(taskDTO.getInspectorId());
        existingTask.setPlanTime(taskDTO.getScheduledTime());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setUpdateTime(LocalDateTime.now());
        
        inspectionTaskMapper.updateById(existingTask);
        
        log.info("更新任务成功: {} - {}", taskId, existingTask.getName());
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        InspectionTask task = inspectionTaskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在: " + taskId);
        }
        
        if ("completed".equals(task.getStatus())) {
            throw new RuntimeException("已完成的任务不能删除");
        }
        
        inspectionTaskMapper.deleteById(taskId);
        
        log.info("删除任务成功: {} - {}", taskId, task.getName());
    }

    @Override
    public TaskScheduleDTO getTaskDetail(Long taskId) {
        InspectionTask task = inspectionTaskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在: " + taskId);
        }
        
        return convertToDTO(task);
    }

    @Override
    public TaskConflictCheckDTO checkScheduleConflict(Long inspectorId, LocalDateTime scheduledTime, 
                                                     Long excludeTaskId) {
        
        // 检查同一时间段是否有其他任务
        QueryWrapper<InspectionTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inspector_id", inspectorId);
        
        // 检查前后1小时内是否有任务
        LocalDateTime startTime = scheduledTime.minusHours(1);
        LocalDateTime endTime = scheduledTime.plusHours(1);
        
        queryWrapper.between("plan_time", startTime, endTime);
        queryWrapper.ne("status", "cancelled"); // 排除已取消的任务
        
        if (excludeTaskId != null) {
            queryWrapper.ne("id", excludeTaskId);
        }
        
        List<InspectionTask> conflictTasks = inspectionTaskMapper.selectList(queryWrapper);
        
        if (!conflictTasks.isEmpty()) {
            InspectionTask conflictTask = conflictTasks.get(0);
            TaskScheduleDTO conflictDTO = convertToDTO(conflictTask);
            
            String message = String.format("该巡检员在 %s 已有任务：%s", 
                    conflictTask.getPlanTime(), conflictTask.getName());
            
            return TaskConflictCheckDTO.hasConflict(conflictDTO, message);
        }
        
        return TaskConflictCheckDTO.noConflict();
    }

    @Override
    public List<TaskScheduleDTO> getTasksByDateRange(LocalDate startDate, LocalDate endDate) {
        QueryWrapper<InspectionTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("plan_time", 
                startDate.atStartOfDay(), 
                endDate.atTime(23, 59, 59));
        queryWrapper.orderByAsc("plan_time");
        
        List<InspectionTask> tasks = inspectionTaskMapper.selectList(queryWrapper);
        
        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<String, Object> batchCreateTasks(List<TaskCreateDTO> batchTasks) {
        int successCount = 0;
        int failureCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (int i = 0; i < batchTasks.size(); i++) {
            try {
                createTask(batchTasks.get(i));
                successCount++;
            } catch (Exception e) {
                failureCount++;
                errors.add(String.format("第%d个任务创建失败: %s", i + 1, e.getMessage()));
                log.error("批量创建任务失败 - 索引: {}, 错误: {}", i, e.getMessage());
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", batchTasks.size());
        result.put("success", successCount);
        result.put("failure", failureCount);
        result.put("errors", errors);
        
        return result;
    }

    @Override
    public Map<String, Object> getInspectorStats(Long inspectorId, String startDate, String endDate) {
        QueryWrapper<InspectionTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inspector_id", inspectorId);
        
        if (startDate != null) {
            queryWrapper.ge("plan_time", startDate);
        }
        if (endDate != null) {
            queryWrapper.le("plan_time", endDate);
        }
        
        List<InspectionTask> tasks = inspectionTaskMapper.selectList(queryWrapper);
        
        // 统计各种状态的任务数量 - 修正类型转换问题
        Map<String, Long> statusStats = new HashMap<>();
        Map<String, List<InspectionTask>> groupedByStatus = tasks.stream()
                .collect(Collectors.groupingBy(task -> 
                    task.getStatus() != null ? task.getStatus() : "unknown"));
        
        for (Map.Entry<String, List<InspectionTask>> entry : groupedByStatus.entrySet()) {
            statusStats.put(entry.getKey(), (long) entry.getValue().size());
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalTasks", tasks.size());
        result.put("statusStats", statusStats);
        result.put("pendingTasks", statusStats.getOrDefault("pending", 0L));
        result.put("completedTasks", statusStats.getOrDefault("completed", 0L));
        result.put("inProgressTasks", statusStats.getOrDefault("in_progress", 0L));
        
        return result;
    }

    /**
     * 将InspectionTask转换为TaskScheduleDTO
     */
    private TaskScheduleDTO convertToDTO(InspectionTask task) {
        TaskScheduleDTO dto = new TaskScheduleDTO();
        
        // 手动映射字段，避免字段名不匹配的问题
        dto.setId(task.getId());
        dto.setTaskName(task.getName()); // name -> taskName
        dto.setAreaId(task.getPointId()); // pointId -> areaId
        dto.setInspectorId(task.getInspectorId());
        dto.setScheduledTime(task.getPlanTime()); // planTime -> scheduledTime
        dto.setStatus(task.getStatus());
        dto.setDescription(task.getDescription());
        dto.setCreateTime(task.getCreateTime());
        dto.setUpdateTime(task.getUpdateTime());
        dto.setPriority("normal"); // 默认优先级，因为InspectionTask没有priority字段
        
        // 获取区域信息
        if (task.getPointId() != null) {
            Area area = areaMapper.selectById(task.getPointId());
            if (area != null) {
                dto.setAreaName(area.getName());
                dto.setAreaCode(area.getAreaCode());
            }
        }
        
        // 获取巡检员信息
        if (task.getInspectorId() != null) {
            User inspector = userMapper.selectById(task.getInspectorId());
            if (inspector != null) {
                dto.setInspectorName(inspector.getRealName());
            }
        }
        
        return dto;
    }
} 